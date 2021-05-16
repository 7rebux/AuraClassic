package net.rebux.auraclassic.listeners

import net.rebux.auraclassic.utils.*
import net.rebux.auraclassic.AuraClassic as ac
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

        // set tablist
        TablistUtil.setTablistName(event.player)
        TablistUtil.setTablistHeaderFooter(event.player)
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent)
    {
        event.quitMessage = ""

        if (arrayListOf(GameState.PRE_GAME, GameState.POST_GAME).contains(ac.instance.gameState))
            event.quitMessage = ConfigUtil.getMessage("quit").replace("{player}", event.player.name)
        else if (ac.instance.gameState == GameState.INGAME && ac.instance.players.contains(event.player))
        {
            ac.instance.players.remove(event.player)

            if (ac.instance.lastHitBy.containsKey(event.player))
            {
                Bukkit.broadcastMessage(ConfigUtil.getMessage("kill")
                    .replace("{player}", event.player.name)
                    .replace("{killer}", ac.instance.lastHitBy[event.player]!!.name))

                ac.instance.playerKills[ac.instance.lastHitBy[event.player]!!]!!.inc()
            }
            else
                Bukkit.broadcastMessage(ConfigUtil.getMessage("death").replace("{player}", event.player.name))

            if (ConfigUtil.getBoolean("remaining_on_quit") && ac.instance.players.size > 1)
                Bukkit.broadcastMessage(ConfigUtil.getMessage("remaining").replace("{count}", ac.instance.players.size.toString()))

            // check if the last player left
            if (ac.instance.players.size == 1)
                ac.instance.endGame()
        }
        else
            GameUtil.removeSpectator(event.player)
    }
}