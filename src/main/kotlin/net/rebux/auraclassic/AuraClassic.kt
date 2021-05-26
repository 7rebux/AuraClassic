package net.rebux.auraclassic

import net.rebux.auraclassic.command.StatsCommand
import net.rebux.auraclassic.listeners.*
import net.rebux.auraclassic.scheduler.*
import net.rebux.auraclassic.utils.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class AuraClassic: JavaPlugin() {
    private val mainFile = File(dataFolder, "config.yml")
    private val messagesFile = File(dataFolder, "messages.yml")
    val mainConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(mainFile)
    val messagesConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(messagesFile)

    val sqlConnection = SQLConnection()

    private lateinit var lobbyWorld: World
    private lateinit var auraWorld: World
    lateinit var lobbyWorldSpawn: Location
    private lateinit var auraWorldSpawn: Location

    lateinit var waitingScheduler: WaitingScheduler
    lateinit var preGameScheduler: PreGameScheduler
    lateinit var protectionScheduler: ProtectionScheduler
    lateinit var ingameScheduler: IngameScheduler
    private lateinit var postGameScheduler: PostGameScheduler

    val players = arrayListOf<Player>()
    val spectators = arrayListOf<Player>()
    val lastHitBy = mutableMapOf<Player, Player>()

    lateinit var gameState: GameState

    override fun onEnable() {
        instance = this

        // connect to database
        sqlConnection.connect(
            ConfigUtil.getString("hostname"),
            ConfigUtil.getInt("port").toString(),
            ConfigUtil.getString("database"),
            ConfigUtil.getString("username"),
            ConfigUtil.getString("password"))
        SQLUtil.createTables()

        // initialize locations
        lobbyWorld = Bukkit.createWorld(WorldCreator("lobby"))
        auraWorld = Bukkit.createWorld(WorldCreator("aura_map"))
        lobbyWorldSpawn = Location(lobbyWorld, 216.5, 4.0, 442.5, 270F, 0F)
        auraWorldSpawn = Location(auraWorld, 8.5, 64.5, -8.5, 90F, 0F)
        lobbyWorld.isAutoSave = false
        auraWorld.isAutoSave = false

        // initialize schedulers
        waitingScheduler = WaitingScheduler()
        preGameScheduler = PreGameScheduler()
        protectionScheduler = ProtectionScheduler()
        ingameScheduler = IngameScheduler()
        postGameScheduler = PostGameScheduler()

        // register event listeners
        Bukkit.getPluginManager().registerEvents(MainListener(), this)
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), this)

        Bukkit.getPluginCommand("stats").executor = StatsCommand()

        gameState = GameState.PRE_GAME
        waitingScheduler.start()
    }

    fun startGame() {
        players.addAll(Bukkit.getOnlinePlayers())
        players.forEach { it.inventory.clear() }
        players.forEach { it.level = 0; it.exp = 0F }
        Bukkit.getScheduler().runTaskAsynchronously(this) { players.forEach { player -> ItemUtil.getItems(player.uniqueId).forEach { player.inventory.addItem(it) } } }
        players.forEach { it.inventory.armorContents = ItemUtil.getArmor() }
        players.forEach { it.teleport(auraWorldSpawn) }
        players.forEach { it.bedSpawnLocation = auraWorldSpawn }
        players.forEach { SQLUtil.incrementStat(it.uniqueId, "played") }
        protectionScheduler.start()
        Bukkit.broadcastMessage(ConfigUtil.getMessage("protection_start"))

        gameState = GameState.INGAME
    }

    fun endGame() {
        var winner: Player? = null

        ingameScheduler.stop()

        if (players.size == 1) winner = players[0] else winner // TODO mittelpunkt gewinnt
        Bukkit.broadcastMessage(ConfigUtil.getMessage("win").replace("{player}", winner!!.name))
        SQLUtil.incrementStat(winner.uniqueId, "won")

        spectators.addAll(players)
        spectators.forEach { it.teleport(lobbyWorldSpawn) }
        postGameScheduler.start()

        gameState = GameState.POST_GAME
    }

    fun shutdown() {
        Bukkit.getServer().unloadWorld(auraWorld, true)
        Bukkit.getServer().unloadWorld(lobbyWorld, true)
        sqlConnection.disconnect()
        // TODO restart server
    }

    companion object {
        lateinit var instance: AuraClassic
    }
}