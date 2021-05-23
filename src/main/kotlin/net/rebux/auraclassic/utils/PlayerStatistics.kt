package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import java.util.*

class PlayerStatistics {
    var uuid: UUID? = null
    var rank: Int = 0
    var kills: Int = 0
    var deaths: Int = 0
    var kd: Double = 0.0
    var won: Int = 0
    var played: Int = 0

    constructor(uuid: UUID) {
        query(uuid)
    }

    constructor(rank: Int) {
        query(rank)
    }

    private fun query(uuid: UUID) {
        AuraClassic.instance.sqlConnection.query("""
            SELECT Ranks . *
            FROM (
                SELECT uuid, @rowNumber := @rowNumber + 1 'rank', kills, deaths, ROUND(kills/deaths, 2) 'kd', played, won
                FROM Players, Statistics, (SELECT @rowNumber := 0)R
                WHERE statistics_id = id
                ORDER BY kills DESC
            ) Ranks
            WHERE uuid = '$uuid';
        """.trimIndent()).also { resultSet ->
            run {
                if (!resultSet.next())
                    return

                this.uuid = uuid
                rank = resultSet.getInt("rank")
                kills = resultSet.getInt("kills")
                deaths = resultSet.getInt("deaths")
                kd = resultSet.getDouble("kd")
                won = resultSet.getInt("won")
                played = resultSet.getInt("played")
            }
        }
    }

    private fun query(rank: Int) {
        AuraClassic.instance.sqlConnection.query("""
            SELECT Ranks . *
            FROM (
                SELECT uuid, @rowNumber := @rowNumber + 1 'rank', kills, deaths, ROUND(kills/deaths, 2) 'kd', played, won
                FROM Players, Statistics, (SELECT @rowNumber := 0)R
                WHERE statistics_id = id
                ORDER BY kills DESC
            ) Ranks
            WHERE rank = $rank;
        """.trimIndent()).also { resultSet ->
            run {
                if (!resultSet.next())
                    return

                uuid = UUID.fromString(resultSet.getString("uuid"))
                this.rank = resultSet.getInt("rank")
                kills = resultSet.getInt("kills")
                deaths = resultSet.getInt("deaths")
                kd = resultSet.getDouble("kd")
                won = resultSet.getInt("won")
                played = resultSet.getInt("played")
            }
        }
    }
}