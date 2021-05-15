package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import org.bukkit.Bukkit

class PreGameScheduler(override var delay: Long = 20L) : IScheduler
{
    private val countdown = ConfigUtil.getInt("pre_game_countdown")
    private var time: Int = countdown

    override fun start()
    {
        super.start()
        time = countdown
    }

    override fun run()
    {
        if (Bukkit.getOnlinePlayers().size < ConfigUtil.getInt("min_players"))
        {
            stop()
            Bukkit.getOnlinePlayers().forEach { it.level = 0; it.exp = 0F }
            AuraClassic.instance.waitingScheduler.start()
            return
        }

        if (time == 0)
        {
            stop()
            AuraClassic.instance.startGame()
            return
        }

        if (listOf(60, 30, 15, 10, 5, 4, 3, 2, 1).contains(time))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("pre_game_timer").replace("{time}", time.toString()))

        Bukkit.getOnlinePlayers().forEach { it.level = time; it.exp = time.toFloat() / countdown.toFloat() }

        --time
    }
}