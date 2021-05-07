package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.ChatColor

object ConfigUtil
{
    fun getMessage(name: String): String = ChatColor.translateAlternateColorCodes('&', AuraClassic.instance.messagesConfig.getString(name))
}