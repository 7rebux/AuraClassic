package net.rebux.auraclassic.utils

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ItemSerializer {
    fun itemStackArrayListFromBase64(data: String): ArrayList<ItemStack> {
        val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
        val dataInput = BukkitObjectInputStream(inputStream)
        val length = dataInput.readInt()
        val items = arrayListOf<ItemStack>()

        for (i in 0 until length)
            items.add(dataInput.readObject() as ItemStack)

        dataInput.close()

        return items
    }

    fun itemStackArrayListToBase64(items: ArrayList<ItemStack>): String {
        val outputStream = ByteArrayOutputStream()
        val dataOutput = BukkitObjectOutputStream(outputStream)

        dataOutput.writeInt(items.size)

        for (i in 0 until items.size)
            dataOutput.writeObject(items[i])

        dataOutput.close()

        return Base64Coder.encodeLines(outputStream.toByteArray())
    }
}