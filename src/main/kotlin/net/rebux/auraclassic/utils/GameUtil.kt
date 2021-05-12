package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object GameUtil
{
    fun addSpectator(player: Player)
    {
        AuraClassic.instance.spectators.add(player)
        player.allowFlight
    }

    fun removeSpectator(player: Player)
    {
        AuraClassic.instance.spectators.remove(player)
    }
}