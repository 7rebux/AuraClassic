package net.rebux.auraclassic.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder
{
    private val itemStack: ItemStack

    constructor(material: Material, amount: Int)
    {
        itemStack = ItemStack(material, amount)
    }

    constructor(material: Material)
    {
        itemStack = ItemStack(material, 1)
    }

    fun setDisplayName(displayName: String): ItemBuilder
    {
        val itemMeta: ItemMeta = itemStack.itemMeta
        itemMeta.displayName = displayName
        itemStack.itemMeta = itemMeta
        return this
    }

    fun setDurability(durability: Short): ItemBuilder
    {
        itemStack.durability = durability
        return this
    }

    fun addEnchantment(enchantment: Enchantment, level: Int): ItemBuilder
    {
        itemStack.addUnsafeEnchantment(enchantment, level)
        return this
    }

    fun toItemStack(): ItemStack = itemStack
}