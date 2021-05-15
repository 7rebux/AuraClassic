package net.rebux.auraclassic

import net.rebux.auraclassic.listeners.*
import net.rebux.auraclassic.scheduler.*
import net.rebux.auraclassic.utils.ConfigUtil
import net.rebux.auraclassic.utils.GameState
import net.rebux.auraclassic.utils.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class AuraClassic: JavaPlugin()
{
    private val mainFile = File(dataFolder, "config.yml")
    private val messagesFile = File(dataFolder, "messages.yml")
    val mainConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(mainFile)
    val messagesConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(messagesFile)

//    private val lobbyWorld: World = Bukkit.createWorld(WorldCreator("world"))
//    private val auraWorld: World = Bukkit.createWorld(WorldCreator("aura_map"))
//    private val lobbyWorldSpawn: Location = Location(lobbyWorld, 0.0, 60.0, 0.0)
//    private val auraWorldSpawn: Location = Location(auraWorld, 0.0, 64.5, -7.0)

    lateinit var waitingScheduler: WaitingScheduler
    lateinit var preGameScheduler: PreGameScheduler
    lateinit var protectionScheduler: ProtectionScheduler
    lateinit var ingameScheduler: IngameScheduler
    lateinit var postGameScheduler: PostGameScheduler

    val players = arrayListOf<Player>()
    val spectators = arrayListOf<Player>()
    val lastHitBy = mutableMapOf<Player, Player>()
    val playerKills = mutableMapOf<Player, Int>()

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
        players.forEach { it.level = 0; it.exp = 0F }
        players.forEach { player -> ItemUtil.getItems(player.uniqueId).forEach { player.inventory.addItem(it) } }
        players.forEach { it.inventory.armorContents = ItemUtil.getArmor() }
        players.forEach { it.teleport(Location(Bukkit.createWorld(WorldCreator("aura_map")), 0.0, 64.5, -7.0)) }
        players.forEach { playerKills[it] = 0 }
        protectionScheduler.start()
        Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_start"))

        gameState = GameState.INGAME
    }

    fun endGame()
    {
        var winner: Player? = null

        ingameScheduler.stop()

        if (players.size == 1) winner = players[0] else winner // TODO mittelpunkt gewinnt
        Bukkit.broadcastMessage(ConfigUtil.getMessage("win").replace("{player}", winner!!.name))

        spectators.addAll(players)
        spectators.forEach { it.teleport(Location(Bukkit.createWorld(WorldCreator("world")), 0.0, 60.0, 0.0)) }
        // TODO save stats to sql database
        postGameScheduler.start()

        gameState = GameState.POST_GAME
    }
}