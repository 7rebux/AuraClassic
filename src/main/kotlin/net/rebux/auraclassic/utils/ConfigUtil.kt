package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic as ac
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World

object ConfigUtil {
    fun getMessage(name: String): String = ChatColor.translateAlternateColorCodes('&', ac.instance.messagesConfig.getString(name))

    fun getString(name: String): String = ChatColor.translateAlternateColorCodes('&', ac.instance.mainConfig.getString(name))

    fun getInt(name: String): Int = ac.instance.mainConfig.getInt(name)

    fun getBoolean(name: String): Boolean = ac.instance.mainConfig.getBoolean(name)

    fun getLocation(name: String, world: World): Location {
        val section = ac.instance.mainConfig.getConfigurationSection(name)

        return Location(world,
            section.getDouble("x"),
            section.getDouble("y"),
            section.getDouble("z"),
            section.getDouble("yaw").toFloat(),
            section.getDouble("pitch").toFloat()
        )
    }
}