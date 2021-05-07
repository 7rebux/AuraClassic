package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit

interface IScheduler: Runnable
{
    var delay: Long

    val taskId: Int
        get() = Bukkit.getScheduler().scheduleSyncRepeatingTask(AuraClassic.instance, this, delay, delay)

    override fun run()

    fun start() = taskId

    fun stop() = Bukkit.getScheduler().cancelTask(taskId)
}