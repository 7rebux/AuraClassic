package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic as ac
import org.bukkit.ChatColor

object ConfigUtil
{
    fun getMessage(name: String): String = ChatColor.translateAlternateColorCodes('&', ac.instance.messagesConfig.getString(name))

    fun getString(name: String): String = ChatColor.translateAlternateColorCodes('&', ac.instance.mainConfig.getString(name))

    fun getInt(name: String): Int = ac.instance.mainConfig.getInt(name)
}