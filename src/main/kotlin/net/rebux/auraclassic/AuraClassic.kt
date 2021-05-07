package net.rebux.auraclassic

import net.rebux.auraclassic.listeners.ConnectionListener
import net.rebux.auraclassic.scheduler.WaitingScheduler
import net.rebux.auraclassic.utils.GameState
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class AuraClassic: JavaPlugin()
{
    private val mainFile = File(dataFolder, "main.yml")
    private val messagesFile = File(dataFolder, "messages.yml")
    val mainConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(mainFile)
    val messagesConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(messagesFile)

    val players = arrayListOf<Player>()
    val spectators = arrayListOf<Player>()
    val lastHitBy = mapOf<Player, Player>()
    lateinit var gameState: GameState

    companion object
    {
        lateinit var instance: AuraClassic
    }

    override fun onEnable()
    {
        instance = this

        // register event listeners
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), this)

        gameState = GameState.PRE_GAME

        WaitingScheduler().start()
    }
}