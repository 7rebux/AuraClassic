package net.rebux.auraclassic.utils

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemUtil
{
    fun getInventorySortingItem(): ItemStack
    {
        return ItemBuilder(Material.CHEST)
            .setDisplayName("Inventarsortierung") // TODO config file entry
            .toItemStack()
    }

    fun getTrackerItem(): ItemStack
    {
        return ItemBuilder(Material.COMPASS)
            .setDisplayName("Tracker") // TODO config file entry
            .toItemStack()
    }
}