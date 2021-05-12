package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.entity.Player

object TablistUtil
{
    fun setTablistName(player: Player)
    {
        player.playerListName = if (AuraClassic.instance.spectators.contains(player)) "§7${player.name}" else "§8${player.name}" // TODO customizable
    }

    fun setTablistHeaderFooter(player: Player)
    {
        
    }
}