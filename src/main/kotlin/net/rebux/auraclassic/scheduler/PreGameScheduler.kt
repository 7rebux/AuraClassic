package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.utils.ItemUtil
import org.bukkit.Bukkit
import java.util.function.Consumer

class PreGameScheduler(override var delay: Long = 20L) : IScheduler
{
    private var countdown: Int = ConfigUtil.getInt("pre_game_countdown")

    override fun start()
    {
        super.start()
        countdown = ConfigUtil.getInt("pre_game_countdown")
    }

    override fun run()
    {
        if (Bukkit.getOnlinePlayers().size < ConfigUtil.getInt("min_players"))
        {
            stop()
            AuraClassic.instance.waitingScheduler.start()
            return
        }

        if (countdown == 0)
        {
            stop()
            AuraClassic.instance.startGame()
            return
        }

        if (listOf(60, 30, 15, 10, 5, 4, 3, 2, 1).contains(countdown))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("pre_game_timer").replace("{time}", countdown.toString()))

        --countdown
    }
}