package net.rebux.auraclassic.listeners

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerAchievementAwardedEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerPickupItemEvent

class MainListener: Listener
{
    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent)
    {
        if (AuraClassic.instance.spectators.contains(event.player))
        {
            AuraClassic.instance.spectators.forEach{
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
        if (AuraClassic.instance.gameState != GameState.INGAME || AuraClassic.instance.protectionScheduler.running)
            event.isCancelled = true
    }

    @EventHandler
    fun onFoodChange(event: FoodLevelChangeEvent)
    {
        if (AuraClassic.instance.gameState == GameState.INGAME)
            return

        (event.entity as Player).foodLevel = 20 // TODO not working
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
        if (AuraClassic.instance.spectators.contains(event.player))
            event.isCancelled = true
    }

    @EventHandler
    fun onAchievement(event: PlayerAchievementAwardedEvent)
    {
        event.isCancelled = true
    }
}