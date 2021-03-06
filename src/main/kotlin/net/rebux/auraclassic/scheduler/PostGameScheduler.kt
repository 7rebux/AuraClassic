package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import org.bukkit.Bukkit

class PostGameScheduler(override var delay: Long = 20L) : IScheduler {
    private var countdown: Int = ConfigUtil.getInt("post_game_countdown")

    override fun run() {
        if (countdown == 0) {
            stop()
            AuraClassic.instance.shutdown()
            return
        }

        if (listOf(15, 10, 5, 4, 3, 2, 1).contains(countdown))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("post_game_timer").replace("{time}", countdown.toString()))

        --countdown
    }
}