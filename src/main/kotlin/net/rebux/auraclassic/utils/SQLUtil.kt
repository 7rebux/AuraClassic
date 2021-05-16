package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Bukkit

object SQLUtil
{
    private val plugin = AuraClassic.instance
    private val sqlConnection = plugin.sqlConnection

    fun createTables()
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin)
        {
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Inventories(id int NOT NULL, hotbar varchar, PRIMARY KEY(id));")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Statistics(id int NOT NULL, player_uuid varchar(36) NOT NULL, kills int, deaths int, won int, played int, PRIMARY KEY(id));")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Players(uuid varchar(36) NOT NULL, statistics_id int, inventories_id int, PRIMARY KEY(id), FOREIGN KEY(statistics_id) REFERENCES Statistics(id), FOREIGN KEY(inventories_id) REFERENCES Inventories(id));")
        }
    }
}