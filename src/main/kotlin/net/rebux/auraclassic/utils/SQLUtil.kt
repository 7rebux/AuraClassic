package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object SQLUtil {
    private val plugin = AuraClassic.instance
    private val sqlConnection = plugin.sqlConnection

    private const val DEFAULT_HOTBAR: String = "rO0ABXcEAAAACXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAN0AAI9PXQABHR5cGV0AARtZXRhdXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3QABVNUSUNLc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIdAAJbWV0YS10eXBldAAIZW5jaGFudHN1cQB+AAYAAAADdAAISXRlbU1ldGF0AApVTlNQRUNJRklDc3IAN2NvbS5nb29nbGUuY29tbW9uLmNvbGxlY3QuSW1tdXRhYmxlQmlNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAHhxAH4AA3VxAH4ABgAAAAF0AAlLTk9DS0JBQ0t1cQB+AAYAAAABc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAARzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXQABmFtb3VudHVxAH4ABgAAAANxAH4ADHQAC0VOREVSX1BFQVJMc3EAfgAbAAAAIHNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+ACF1cQB+AAYAAAADcQB+AAx0AAlTTk9XX0JBTExzcQB+ABsAAAAQc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AIXVxAH4ABgAAAANxAH4ADHQAC1BVTVBLSU5fUElFc3EAfgAbAAAAFHNxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAGAAAAAnEAfgAMdAAMR09MREVOX0FQUExFc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAl0AAZkYW1hZ2V1cQB+AAYAAAADcQB+AAx0AAtNT05TVEVSX0VHR3NyAA9qYXZhLmxhbmcuU2hvcnRoTTcTNGDaUgIAAVMABXZhbHVleHEAfgAcADJzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgA5dXEAfgAGAAAAA3EAfgAMdAAGUE9USU9Oc3EAfgA8AAVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgA5dXEAfgAGAAAAA3EAfgAMcQB+AEJzcQB+ADxAInNxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAGAAAAAnEAfgAMdAALRklTSElOR19ST0Q="

    fun createTables() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Inventories (id int NOT NULL PRIMARY KEY AUTO_INCREMENT, hotbar varchar(1604));")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Statistics(id int NOT NULL PRIMARY KEY AUTO_INCREMENT, kills int, deaths int, won int, played int);")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Players(uuid varchar(36) NOT NULL PRIMARY KEY, statistics_id int, FOREIGN KEY(statistics_id) REFERENCES Statistics(id), inventories_id int, FOREIGN KEY(inventories_id) REFERENCES Inventories(id));")

            if (!hasDefaultHotbar()) {
                sqlConnection.update("""
                    INSERT INTO Inventories(hotbar)
                    VALUES('$DEFAULT_HOTBAR')
                """.trimIndent())
            }
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

        sqlConnection.update("""
                INSERT INTO Statistics(kills, deaths, won, played)
                VALUES(0, 0, 0, 0);
            """.trimIndent())

        sqlConnection.query("""
            SELECT MAX(id) AS 'id'
            FROM Statistics;
        """.trimIndent()).also { resultSet -> resultSet.next(); id = resultSet.getInt("id") }

        sqlConnection.update("""
                INSERT INTO Players(uuid, statistics_id, inventories_id)
                VALUES('${player.uniqueId}', $id, 1);
            """.trimIndent())
    }

    fun incrementStat(uuid: UUID, column: String) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            sqlConnection.update("""
                UPDATE Statistics, Players
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

    private fun hasHotbar(hotbar: String): Boolean {
        sqlConnection.query("""
            SELECT hotbar
            FROM Inventories
            WHERE hotbar = '$hotbar';
        """.trimIndent()).also { resultSet -> return resultSet.next() }
    }

    private fun hasDefaultHotbar(): Boolean {
        sqlConnection.query("""
            SELECT id
            FROM Inventories
            WHERE id = 1;
        """.trimIndent()).also { resultSet -> return resultSet.next() }
    }

    fun setHotbar(uuid: UUID, hotbar: String) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            val id: Int

            if (!hasHotbar(hotbar)) {
                sqlConnection.update("""
                    INSERT INTO Inventories(hotbar)
                    VALUES('$hotbar')
                """.trimIndent())

                sqlConnection.query("""
                    SELECT MAX(id) AS 'id'
                    FROM Inventories;
                """.trimIndent()).also { resultSet -> resultSet.next(); id = resultSet.getInt("id") }

            } else {
                sqlConnection.query("""
                    SELECT id
                    FROM Inventories
                    WHERE hotbar = '$hotbar'
                """.trimIndent()).also { resultSet -> resultSet.next(); id = resultSet.getInt("id") }
            }

            sqlConnection.update("""
                UPDATE Players
                SET inventories_id = $id
                WHERE uuid = '$uuid'
            """.trimIndent())
        }
    }
}