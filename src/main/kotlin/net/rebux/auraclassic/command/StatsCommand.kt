package net.rebux.auraclassic.command

import net.rebux.auraclassic.AuraClassic
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.PlayerStatistics
import net.rebux.auraclassic.utils.SQLUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class StatsCommand : CommandExecutor {
    /**
     * Executes the given command, returning its success
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        Bukkit.getScheduler().runTaskAsynchronously(AuraClassic.instance) {
            val uuid: UUID
            val stats: PlayerStatistics

            when {
                args.isEmpty() -> {
                    if (sender !is Player) {
                        println("You cannot lookup the statistics of the console")
                        return@runTaskAsynchronously
                    }

                    uuid = Bukkit.getPlayer(sender.name).uniqueId

                    if (SQLUtil.hasPlayer(uuid))
                        stats = PlayerStatistics(uuid)
                    else {
                        sender.sendMessage(ConfigUtil.getMessage("stats_not_found"))
                        return@runTaskAsynchronously
                    }
                }
                args[0].startsWith("#") -> {
                    if (args[0].split("#")[1].matches(Regex("^[0-9]*\$"))) {
                        val rank = Integer.valueOf(args[0].split("#")[1])

                        if (rank in 1..SQLUtil.getMaxRank())
                            stats = PlayerStatistics(rank)
                        else {
                            sender.sendMessage(ConfigUtil.getMessage("stats_invalid_rank"))
                            return@runTaskAsynchronously
                        }
                    } else {
                        sender.sendMessage(ConfigUtil.getMessage("stats_invalid_rank"))
                        return@runTaskAsynchronously
                    }
                }
                else -> {
                    uuid = Bukkit.getOfflinePlayer(args[0]).uniqueId

                    if (SQLUtil.hasPlayer(uuid))
                        stats = PlayerStatistics(uuid)
                    else {
                        sender.sendMessage(ConfigUtil.getMessage("stats_not_found"))
                        return@runTaskAsynchronously
                    }
                }
            }
            sender.sendMessage(ConfigUtil.getMessage("stats_header").replace("{player}", Bukkit.getOfflinePlayer(stats.uuid).name))
            sender.sendMessage(ConfigUtil.getMessage("stats_ranking").replace("{rank}", stats.rank.toString()))
            sender.sendMessage(ConfigUtil.getMessage("stats_kills").replace("{kills}", stats.kills.toString()))
            sender.sendMessage(ConfigUtil.getMessage("stats_deaths").replace("{deaths}", stats.deaths.toString()))
            sender.sendMessage(ConfigUtil.getMessage("stats_kd").replace("{kd}", stats.kd.toString()))
            sender.sendMessage(ConfigUtil.getMessage("stats_won").replace("{won}", stats.won.toString()))
            sender.sendMessage(ConfigUtil.getMessage("stats_played").replace("{played}", stats.played.toString()))
            sender.sendMessage(ConfigUtil.getMessage("stats_footer"))
        }
        return true
    }
}