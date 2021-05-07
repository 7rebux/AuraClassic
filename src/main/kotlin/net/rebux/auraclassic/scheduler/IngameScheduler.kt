package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.AuraClassic as ac
import org.bukkit.Bukkit

class IngameScheduler(override var delay: Long = 20L) : IScheduler
{
    private var countdown: Int = ac.instance.mainConfig.getInt("ingame_countdown")

    override fun run()
    {
        if (countdown == 0)
        {
            stop()
            // TODO end game stuff
            ac.instance.gameState = GameState.POST_GAME
            PostGameScheduler().start()
            return
        }

        if (countdown == ac.instance.mainConfig.getInt("ingame_countdown") - ac.instance.mainConfig.getInt("tracker_countdown"))
        {
            // TODO give tracker
            Bukkit.broadcastMessage(ConfigUtil.getMessage("tracker_info"))
        }

        if (countdown == 60)
            Bukkit.broadcastMessage(ConfigUtil.getMessage("ingame_end_info"))

        if (listOf(90, 80, 70, 60, 50, 40, 30, 20, 15, 10, 5, 4, 3, 2, 1).contains(countdown))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("ingame_end_timer").replace("{time}", countdown.toString()))

        --countdown
    }
}