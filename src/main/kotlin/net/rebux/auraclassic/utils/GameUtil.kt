package net.rebux.auraclassic.utils

import net.rebux.auraclassic.AuraClassic
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object GameUtil {
    fun addSpectator(player: Player) {
        AuraClassic.instance.spectators.add(player)
        player.allowFlight
        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 0, false, false))
    }

    fun removeSpectator(player: Player) {
        AuraClassic.instance.spectators.remove(player)
    }

    fun getClosestPlayerToLocation(location: Location, players: ArrayList<Player>): Player {
        var closest: Player = players[0]

        for (player in players) {
            if (location.distanceSquared(player.location) < closest.location.distanceSquared(location))
                closest = player
        }

        return closest
    }
}