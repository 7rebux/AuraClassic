package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.utils.ConfigUtil
import org.bukkit.Bukkit
import net.rebux.auraclassic.AuraClassic as ac

class WaitingScheduler(override var delay: Long = 20L) : IScheduler
{
    private var countdown = 0

    override fun run()
    {
        if (Bukkit.getOnlinePlayers().size == ac.instance.mainConfig.getInt("min_players"))
        {
            stop()
            PreGameScheduler().start()
            return
        }

        if (countdown % ac.instance.mainConfig.getInt("waiting_countdown") == 0)
            Bukkit.broadcastMessage(ConfigUtil.getMessage("waiting"))

        ++countdown
    }
}