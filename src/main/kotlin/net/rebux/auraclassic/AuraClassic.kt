package net.rebux.auraclassic

import net.rebux.auraclassic.listeners.*
import net.rebux.auraclassic.scheduler.*
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.utils.ItemUtil
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

    lateinit var waitingScheduler: WaitingScheduler
    lateinit var preGameScheduler: PreGameScheduler
    lateinit var protectionScheduler: ProtectionScheduler
    lateinit var ingameScheduler: IngameScheduler
    lateinit var postGameScheduler: PostGameScheduler

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

        // initialize schedulers
        waitingScheduler = WaitingScheduler()
        preGameScheduler = PreGameScheduler()
        protectionScheduler = ProtectionScheduler()
        ingameScheduler = IngameScheduler()
        postGameScheduler = PostGameScheduler()

        // register event listeners
        Bukkit.getPluginManager().registerEvents(MainListener(), this)
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), this)

        gameState = GameState.PRE_GAME

        waitingScheduler.start()
    }

    fun startGame()
    {
        players.addAll(Bukkit.getOnlinePlayers())
        players.forEach { it.inventory.clear() }
        players.forEach { player -> ItemUtil.getItems(player.uniqueId).forEach { player.inventory.addItem(it) } }
        players.forEach { it.inventory.armorContents = ItemUtil.getArmor() }
        protectionScheduler.start()
        Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_start"))

        gameState = GameState.INGAME
    }
}