package net.rebux.auraclassic.listeners

import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.AuraClassic as ac
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.utils.GameUtil
import net.rebux.auraclassic.utils.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ConnectionListener: Listener
{
    @EventHandler
    fun onConnect(event: PlayerJoinEvent)
    {
        if (ac.instance.gameState == GameState.PRE_GAME)
        {
            event.joinMessage = ConfigUtil.getMessage("join").replace("{player}", event.player.name)
            event.player.inventory.setItem(8, ItemUtil.getInventorySortingItem())
        }
        else
        {
            GameUtil.addSpectator(event.player)
            event.joinMessage = ""
        }
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent)
    {
        if (arrayListOf(GameState.PRE_GAME, GameState.POST_GAME).contains(ac.instance.gameState))
            event.quitMessage = ConfigUtil.getMessage("quit").replace("{player}", event.player.name)
        else if (ac.instance.gameState == GameState.INGAME && ac.instance.players.contains(event.player))
        {
            // remove from players
            ac.instance.players.remove(event.player)

            event.quitMessage = ConfigUtil.getMessage("death").replace("{player}", event.player.name) // TODO kill falls gehittet worden
            Bukkit.broadcastMessage(ConfigUtil.getMessage("remaining").replace("{count}", ac.instance.players.size.toString())) // this is not a thing on gommehd.net
        }
        else
        {
            GameUtil.removeSpectator(event.player)
            event.quitMessage = ""
        }
    }
}