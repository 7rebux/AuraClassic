package net.rebux.auraclassic.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import kotlin.collections.ArrayList

object ItemUtil {
    fun getInventorySortingItem(): ItemStack {
        return ItemBuilder(Material.CHEST)
            .setDisplayName(ConfigUtil.getString("inventory_sorting_name"))
            .toItemStack()
    }

    fun getItems(uuid: UUID): ArrayList<ItemStack> = ItemSerializer.itemStackArrayListFromBase64(SQLUtil.getHotbar(uuid));

    fun getArmor(): Array<ItemStack?> {
        val items = arrayOfNulls<ItemStack>(4)

        items[0] = ItemBuilder(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_FALL, 10).toItemStack()
        items[1] = ItemBuilder(Material.IRON_LEGGINGS).toItemStack()
        items[2] = ItemBuilder(Material.IRON_CHESTPLATE).toItemStack()
        items[3] = ItemBuilder(Material.IRON_HELMET).toItemStack()

        return items
    }

    fun getTrackerItem(): ItemStack {
        return ItemBuilder(Material.COMPASS)
            .setDisplayName(ConfigUtil.getString("tracker_name"))
            .toItemStack()
    }

    fun getSkullItem(owner: Player): ItemStack {
        val skull = ItemStack(Material.SKULL_ITEM)
        val skullMeta = skull.itemMeta as SkullMeta

        skullMeta.displayName = owner.name
        skullMeta.owner = owner.name

        skull.itemMeta = skullMeta

        return skull
    }
}