package net.rebux.auraclassic.listeners

import net.rebux.auraclassic.AuraClassic as ac
import net.rebux.auraclassic.utils.GameState
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
            event.joinMessage = getEntry("join").replace("{player}", event.player.name)
        else
            ac.instance.spectators.add(event.player)
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent)
    {
        if (arrayListOf(GameState.PRE_GAME, GameState.POST_GAME).contains(ac.instance.gameState))
            event.quitMessage = getEntry("quit").replace("{player}", event.player.name)
        else if (ac.instance.gameState == GameState.INGAME && ac.instance.players.contains(event.player))
        {
            // remove from players
            ac.instance.players.remove(event.player)

            event.quitMessage = getEntry("death").replace("{player}", event.player.name) // TODO kill falls gehittet worden
            Bukkit.broadcastMessage(getEntry("remaining").replace("{count}", ac.instance.players.size.toString())) // this is not a thing on gommehd.net
        }
        else
        {
            // remove from spectators
            ac.instance.spectators.remove(event.player)

            event.quitMessage = ""
        }
    }

    private fun getEntry(name: String) = ac.instance.messagesConfig.getString(name)
}