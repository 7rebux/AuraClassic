package net.rebux.auraclassic.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerAchievementAwardedEvent
import org.bukkit.event.player.PlayerDropItemEvent

class IngameListener: Listener
{
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
    fun onExplode(event: EntityExplodeEvent)
    {
        event.isCancelled = true
    }

    @EventHandler
    fun onAchievement(event: PlayerAchievementAwardedEvent)
    {
        event.isCancelled = true
    }
}