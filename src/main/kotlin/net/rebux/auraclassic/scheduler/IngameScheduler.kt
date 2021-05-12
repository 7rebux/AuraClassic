package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.utils.ItemUtil
import org.bukkit.Bukkit
import java.util.function.Consumer

class IngameScheduler(override var delay: Long = 20L) : IScheduler
{
    private var countdown: Int = ConfigUtil.getInt("ingame_countdown")

    override fun run()
    {
        if (countdown == 0 || AuraClassic.instance.players.size == 1)
        {
            AuraClassic.instance.endGame()
            return
        }

        if (countdown == ConfigUtil.getInt("ingame_countdown") - ConfigUtil.getInt("tracker_countdown"))
        {
            AuraClassic.instance.players.forEach(Consumer { it.inventory.addItem(ItemUtil.getTrackerItem()) })
            Bukkit.broadcastMessage(ConfigUtil.getMessage("tracker_info"))
        }

        if (countdown == 60)
            Bukkit.broadcastMessage(ConfigUtil.getMessage("ingame_end_info"))

        if (listOf(90, 80, 70, 60, 50, 40, 30, 20, 15, 10, 5, 4, 3, 2, 1).contains(countdown))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("ingame_end_timer").replace("{time}", countdown.toString()))

        --countdown
    }
}