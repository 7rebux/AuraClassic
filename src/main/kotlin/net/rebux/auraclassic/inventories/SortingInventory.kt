package net.rebux.auraclassic.inventories

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class SortingInventory(private val player: Player) {
    val inventory: Inventory

    init {
        inventory = Bukkit.createInventory(null, 9, ConfigUtil.getString("inventory_sorting_name_inventory"))
        loadContents()
    }

    private fun loadContents() {
        Bukkit.getScheduler().runTaskAsynchronously(AuraClassic.instance) {
            ItemUtil.getItems(player.uniqueId).forEach { inventory.addItem(it) }
        }
    }
}