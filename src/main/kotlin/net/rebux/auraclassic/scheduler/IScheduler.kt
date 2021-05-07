package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit

interface IScheduler: Runnable
{
    companion object
    {
        var taskId: Int = 0
    }

    var delay: Long

    override fun run()

    fun start()
    {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AuraClassic.instance, this, delay, delay)
    }

    fun stop() = Bukkit.getScheduler().cancelTask(taskId)
}