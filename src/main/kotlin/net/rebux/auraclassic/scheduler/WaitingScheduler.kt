package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import org.bukkit.Bukkit

class WaitingScheduler(override var delay: Long = 20L) : IScheduler
{
    private var countdown = 0

    override fun run()
    {
        if (Bukkit.getOnlinePlayers().size >= ConfigUtil.getInt("min_players"))
        {
            stop()
            AuraClassic.instance.preGameScheduler.start()
            return
        }

        if (countdown % ConfigUtil.getInt("waiting_delay") == 0)
            Bukkit.broadcastMessage(ConfigUtil.getMessage("waiting"))

        ++countdown
    }
}