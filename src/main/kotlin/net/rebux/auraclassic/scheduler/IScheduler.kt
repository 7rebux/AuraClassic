package net.rebux.auraclassic.scheduler

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit

interface IScheduler: Runnable {
    var delay: Long

    override fun run()

    fun start() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AuraClassic.instance, this, delay, delay)
    }

    fun stop() {
        Bukkit.getScheduler().cancelTask(taskId)
    }

    companion object {
        var taskId = -1 // TODO thats really fkn bad fix fast :)
    }
}