package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object SQLUtil {
    private val plugin = AuraClassic.instance
    private val sqlConnection = plugin.sqlConnection

    fun createTables() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Inventories (id int NOT NULL PRIMARY KEY AUTO_INCREMENT, hotbar varchar(255));")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Statistics(id int NOT NULL PRIMARY KEY AUTO_INCREMENT, kills int, deaths int, won int, played int);")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Players(uuid varchar(36) NOT NULL PRIMARY KEY, statistics_id int, FOREIGN KEY(statistics_id) REFERENCES Statistics(id), inventories_id int, FOREIGN KEY(inventories_id) REFERENCES Inventories(id));")
        }
    }

    fun hasPlayer(uuid: UUID): Boolean {
        sqlConnection.query("""
            SELECT uuid
            FROM Players
            WHERE uuid = '$uuid';
        """.trimIndent()).also { resultSet -> return resultSet.next() }
    }

    fun getMaxRank(): Int {
        sqlConnection.query("""
            SELECT MAX(id)+1 as 'rank'
            FROM Statistics;
        """.trimIndent()).also { resultSet -> resultSet.next(); return resultSet.getInt("rank") }
    }

    fun addPlayer(player: Player) {
        val id: Int

        sqlConnection.query("""
                INSERT INTO Statistics(kills, deaths, won, played)
                VALUES(0, 0, 0, 0)
                SELECT LAST_INSERT_ID() as 'id';
            """.trimIndent()).also { resultSet -> resultSet.next(); id = resultSet.getInt("id") }

        sqlConnection.update("""
                INSERT INTO Players(uuid, statistics_id, inventory_id)
                VALUES(${player.uniqueId}, $id, 0);
            """.trimIndent())
    }

    fun incrementStat(uuid: UUID, column: String) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            sqlConnection.update("""
                UPDATE statistics, Players
                SET $column = $column + 1
                WHERE statistics_id = id
	                AND uuid = '$uuid'
            """.trimIndent())
        }
    }

    fun getHotbar(uuid: UUID): String {
        sqlConnection.query("""
            SELECT hotbar
            FROM Players, Inventories
            WHERE inventories_id = id
                AND uuid = '$uuid'
        """.trimIndent()).also { resultSet -> resultSet.next(); return resultSet.getString("hotbar") }
    }

    fun addHotbar(uuid: UUID) {

    }
}