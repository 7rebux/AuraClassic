package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object GameUtil
{
    fun startGame()
    {

    }

    fun addSpectator(player: Player)
    {
        AuraClassic.instance.spectators.add(player)
        player.allowFlight
    }

    fun removeSpectator(player: Player)
    {
        AuraClassic.instance.spectators.remove(player)
    }

    fun checkForWin(): Player?
    {
        return if (AuraClassic.instance.players.size == 1)
            AuraClassic.instance.players[0]
        else
            null
    }

    fun showWinner(player: Player)
    {
        Bukkit.broadcastMessage(ConfigUtil.getMessage("win").replace("{player}", player.name))
    }
}