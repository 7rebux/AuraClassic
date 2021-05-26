package net.rebux.auraclassic.listeners

import net.rebux.auraclassic.inventories.SortingInventory
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.utils.ItemSerializer
import net.rebux.auraclassic.utils.SQLUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.*
import org.bukkit.event.weather.WeatherChangeEvent
import net.rebux.auraclassic.AuraClassic as ac

class MainListener: Listener {
    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (event.entity is ItemFrame) {
            event.isCancelled = true
            return
        }

        if (event.damager.type != EntityType.PLAYER || event.entity !is Player)
            return

        ac.instance.lastHitBy[event.entity as Player] = event.damager as Player
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        event.deathMessage = ""

        if (ac.instance.gameState != GameState.INGAME)
            return

        ac.instance.players.remove(event.entity)

        if (event.entity.killer != null) {
            Bukkit.broadcastMessage(ConfigUtil.getMessage("kill")
                .replace("{player}", event.entity.name)
                .replace("{killer}", event.entity.killer.name))

            SQLUtil.incrementStat(event.entity.killer.uniqueId, "kills")
            SQLUtil.incrementStat(event.entity.uniqueId, "deaths")
        } else {
            if (ac.instance.lastHitBy.containsKey(event.entity)) {
                Bukkit.broadcastMessage(ConfigUtil.getMessage("kill")
                    .replace("{player}", event.entity.name)
                    .replace("{killer}", ac.instance.lastHitBy[event.entity]!!.name))

                SQLUtil.incrementStat(ac.instance.lastHitBy[event.entity]!!.uniqueId, "kills")
                SQLUtil.incrementStat(event.entity.uniqueId, "deaths")
            }
            else
                Bukkit.broadcastMessage(ConfigUtil.getMessage("death").replace("{player}", event.entity.name))
        }

        if (ac.instance.players.size > 1)
            Bukkit.broadcastMessage(ConfigUtil.getMessage("remaining").replace("{count}", ac.instance.players.size.toString()))

        // check if the last player died
        if (ac.instance.players.size == 1)
            ac.instance.endGame()
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        if (ac.instance.spectators.contains(event.player)) {
            ac.instance.spectators.forEach{
                it.sendMessage(ConfigUtil.getMessage("chat_spectator")
                    .replace("{player}", event.player.name)
                    .replace("{message}", event.message))
            }
            event.isCancelled = true
        }
        else
            event.format = ConfigUtil.getMessage("chat").replace("{player}", "%1\$s").replace("{message}", "%2\$s")
    }

    @EventHandler
    fun onRightClickBlock(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return

        if (listOf(
                Material.CHEST,
                Material.HOPPER,
                Material.DISPENSER,
                Material.TRAPPED_CHEST,
                Material.ANVIL,
                Material.WORKBENCH,
                Material.FURNACE,
                Material.ENDER_CHEST,
                Material.DROPPER
            ).contains(event.clickedBlock.type))
            event.isCancelled = true
    }

    @EventHandler
    fun onRightClickItem(event: PlayerInteractEvent) {
        if (event.player.itemInHand?.itemMeta?.displayName == ConfigUtil.getString("inventory_sorting_name"))
            event.player.openInventory(SortingInventory(event.player).inventory)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (ac.instance.gameState == GameState.INGAME)
            return

        if (event.inventory.name == ConfigUtil.getString("inventory_sorting_name_inventory")) {
            if (!listOf(InventoryAction.PICKUP_ALL, InventoryAction.PLACE_ALL, InventoryAction.SWAP_WITH_CURSOR).contains(event.action))
                event.isCancelled = true
            else
                if (listOf(InventoryAction.SWAP_WITH_CURSOR, InventoryAction.PLACE_ALL).contains(event.action)) {
                    if (event.rawSlot > 8)
                        event.isCancelled = true
                }
        } else
            event.isCancelled = true
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (event.inventory.name == ConfigUtil.getString("inventory_sorting_name_inventory")) {
            val data: String = ItemSerializer.itemStackArrayListToBase64(arrayListOf(*event.inventory.contents))
            if (data != "rO0ABXcEAAAACXBwcHBwcHBwcA==") // check if inventory is not empty
                SQLUtil.setHotbar(event.player.uniqueId, data)
        }
    }

    @EventHandler
    fun onHangingBreak(event: HangingBreakEvent) {
        //TODO still drop the item tho
        if (event.cause == HangingBreakEvent.RemoveCause.ENTITY)
            event.isCancelled = true
    }

    @EventHandler
    fun onTrample(event: PlayerInteractEvent) {
        // TODO allow mobs to trample tho (maybe mobGriefing = false)
        if (event.action == Action.PHYSICAL && event.clickedBlock.type == Material.SOIL)
            event.isCancelled = true
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (ac.instance.gameState != GameState.INGAME || ac.instance.protectionScheduler.running)
            event.isCancelled = true
    }

    @EventHandler
    fun onFoodChange(event: FoodLevelChangeEvent) {
        if (ac.instance.gameState == GameState.INGAME)
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onWeatherChange(event: WeatherChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPickup(event: PlayerPickupItemEvent) {
        if (ac.instance.spectators.contains(event.player))
            event.isCancelled = true
    }

    @EventHandler
    fun onAchievement(event: PlayerAchievementAwardedEvent) {
        event.isCancelled = true
    }
}