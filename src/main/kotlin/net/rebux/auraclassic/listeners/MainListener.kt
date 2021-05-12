package net.rebux.auraclassic.listeners

import net.rebux.auraclassic.AuraClassic as ac
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerAchievementAwardedEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerPickupItemEvent

class MainListener: Listener
{
    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent)
    {
        if (event.damager.type != EntityType.PLAYER)
            return

        ac.instance.lastHitBy[event.entity as Player] = event.damager as Player
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent)
    {
        event.deathMessage = ""

        if (ac.instance.gameState != GameState.INGAME)
            return

        ac.instance.players.remove(event.entity)

        if (event.entity.killer != null)
        {
            Bukkit.broadcastMessage(ConfigUtil.getMessage("kill")
                .replace("{player}", event.entity.name)
                .replace("{killer}", event.entity.killer.name))

            ac.instance.playerKills[event.entity.killer]!!.inc()
        }
        else
        {
            if (ac.instance.lastHitBy.containsKey(event.entity))
            {
                Bukkit.broadcastMessage(ConfigUtil.getMessage("kill")
                    .replace("{player}", event.entity.name)
                    .replace("{killer}", ac.instance.lastHitBy[event.entity]!!.name))

                ac.instance.playerKills[ac.instance.lastHitBy[event.entity]!!]!!.inc()
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
    fun onChat(event: AsyncPlayerChatEvent)
    {
        if (ac.instance.spectators.contains(event.player))
        {
            ac.instance.spectators.forEach{
                it.sendMessage(ConfigUtil.getMessage("chat_spectator")
                    .replace("{player}", event.player.name)
                    .replace("{message}", event.message))
            } // TODO there is also AsyncPlayerChatEvent#recipients
            event.isCancelled = true
        }
        else
            event.format = ConfigUtil.getMessage("chat").replace("{player}", "%1\$s").replace("{message}", "%2\$s")
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent)
    {
        if (ac.instance.gameState != GameState.INGAME || ac.instance.protectionScheduler.running)
            event.isCancelled = true
    }

    @EventHandler
    fun onFoodChange(event: FoodLevelChangeEvent)
    {
        if (ac.instance.gameState == GameState.INGAME)
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent)
    {
        event.isCancelled = true
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent)
    {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlace(event: BlockPlaceEvent)
    {
        event.isCancelled = true
    }

    @EventHandler
    fun onPickup(event: PlayerPickupItemEvent)
    {
        if (ac.instance.spectators.contains(event.player))
            event.isCancelled = true
    }

    @EventHandler
    fun onAchievement(event: PlayerAchievementAwardedEvent)
    {
        event.isCancelled = true
    }
}