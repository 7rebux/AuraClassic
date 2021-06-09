package net.rebux.auraclassic.commands

import net.rebux.auraclassic.AuraClassic
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StartCommand : CommandExecutor {
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
        if (sender.hasPermission("aura.start"))
            AuraClassic.instance.startGame()

        return true
    }
}
