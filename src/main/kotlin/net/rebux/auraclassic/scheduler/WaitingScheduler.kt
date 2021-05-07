package net.rebux.auraclassic.scheduler

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
            Bukkit.broadcastMessage(ac.instance.messagesConfig.getString("waiting"))

        ++countdown
    }
}