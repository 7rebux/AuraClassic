package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.utils.ConfigUtil
import org.bukkit.Bukkit
import net.rebux.auraclassic.AuraClassic as ac

class ProtectionScheduler(override var delay: Long = 20L) : IScheduler
{
    private var countdown: Int = ac.instance.mainConfig.getInt("protection_countdown")

    override fun run()
    {
        if (countdown == 0)
        {
            Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_end"))
            stop()
            IngameScheduler().start()
            return
        }

        if (listOf(20, 15, 10, 5, 4, 3, 2, 1).contains(countdown))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_timer").replace("{time}", countdown.toString()))

        --countdown
    }
}