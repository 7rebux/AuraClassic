package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.ItemUtil
import org.bukkit.Bukkit

class IngameScheduler(override var delay: Long = 20L) : IScheduler {
    private val countdown: Int = ConfigUtil.getInt("ingame_countdown")
    private var time: Int = countdown

    override fun run() {
        if (time == 0 || AuraClassic.instance.players.size == 1) {
            AuraClassic.instance.endGame()
            return
        }

        if (time == countdown - ConfigUtil.getInt("tracker_countdown"))
            giveTracker()

        if (time == 60)
            Bukkit.broadcastMessage(ConfigUtil.getMessage("ingame_end_info"))

        if (listOf(90, 80, 70, 60, 50, 40, 30, 20, 15, 10, 5, 4, 3, 2, 1).contains(time))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("ingame_end_timer").replace("{time}", time.toString()))

        --time
    }

    private fun giveTracker() {
        AuraClassic.instance.players.forEach { it.inventory.addItem(ItemUtil.getTrackerItem()) }
        Bukkit.broadcastMessage(ConfigUtil.getMessage("tracker_info"))
    }
}