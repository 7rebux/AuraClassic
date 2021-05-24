package net.rebux.auraclassic.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.collections.ArrayList

object ItemUtil {
    fun getInventorySortingItem(): ItemStack {
        return ItemBuilder(Material.CHEST)
            .setDisplayName(ConfigUtil.getString("inventory_sorting_name"))
            .toItemStack()
    }

    fun getItems(uuid: UUID): ArrayList<ItemStack> {
        val items = arrayListOf<ItemStack>()

        items.add(ItemBuilder(Material.STICK).addEnchantment(Enchantment.KNOCKBACK, 4).toItemStack())
        items.add(ItemBuilder(Material.ENDER_PEARL, 32).toItemStack())
        items.add(ItemBuilder(Material.SNOW_BALL, 16).toItemStack())
        items.add(ItemBuilder(Material.PUMPKIN_PIE, 20).toItemStack())
        items.add(ItemBuilder(Material.GOLDEN_APPLE).toItemStack())
        items.add(ItemBuilder(Material.MONSTER_EGG).setDurability(50).toItemStack())
        items.add(ItemBuilder(Material.POTION).setDurability(5).toItemStack())
        items.add(ItemBuilder(Material.POTION).setDurability(16418).toItemStack())
        items.add(ItemBuilder(Material.FISHING_ROD).toItemStack())

        return items
    }

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
}