package net.rebux.auraclassic.inventories

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory

class CompassInventory {
    fun getInventory(): Inventory {
        val inventory = Bukkit.createInventory(null, 9)

        AuraClassic.instance.players.forEach { inventory.addItem(ItemUtil.getSkullItem(it)) }

        return inventory;
    }
}