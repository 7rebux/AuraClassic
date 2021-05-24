package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import org.bukkit.Bukkit

class ProtectionScheduler(override var delay: Long = 20L) : IScheduler {
    private var countdown: Int = ConfigUtil.getInt("protection_countdown")
    var running = false

    override fun run() {
        if (countdown == 0) {
            Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_end"))
            stop()
            AuraClassic.instance.ingameScheduler.start()
            return
        }

        if (listOf(20, 15, 10, 5, 4, 3, 2, 1).contains(countdown))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_timer").replace("{time}", countdown.toString()))

        --countdown
    }

    override fun start() {
        super.start()
        running = true
    }

    override fun stop() {
        super.stop()
        running = false
    }
}