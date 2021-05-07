package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.AuraClassic as ac
import org.bukkit.Bukkit

class PreGameScheduler(override var delay: Long = 20L) : IScheduler
{
    private var countdown: Int = ac.instance.mainConfig.getInt("pre_game_countdown")

    override fun run()
    {
        if (Bukkit.getOnlinePlayers().size < ac.instance.mainConfig.getInt("min_players"))
        {
            stop()
            WaitingScheduler()
            return
        }

        if (countdown == 0)
        {
            stop()
            // TODO start game stuff
            ac.instance.gameState = GameState.INGAME
            Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_start"))
            ProtectionScheduler().start()
            return
        }

        if (listOf(60, 30, 15, 10, 5, 4, 3, 2, 1).contains(countdown))
            Bukkit.broadcastMessage(ConfigUtil.getMessage("pre_game_timer").replace("{time}", countdown.toString()))

        --countdown
    }
}