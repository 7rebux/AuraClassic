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
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Inventories (id int NOT NULL PRIMARY KEY, hotbar varchar(255));")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Statistics(id int NOT NULL PRIMARY KEY, kills int, deaths int, won int, played int);")
            sqlConnection.update("CREATE TABLE IF NOT EXISTS Players(uuid varchar(36) NOT NULL PRIMARY KEY, statistics_id int, FOREIGN KEY(statistics_id) REFERENCES Statistics(id), inventories_id int, FOREIGN KEY(inventories_id) REFERENCES Inventories(id));")
        }
    }
}