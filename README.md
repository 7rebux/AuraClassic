<h1 align="center">WORK IN PROGRESS!</h1>

## Aura Classic Plugin
A minecraft plugin of the famous game mode **Aura Classic** made in pure [Kotlin](https://kotlinlang.org/) with the [Bukkit API](https://getbukkit.org/)

## Currently Supported Minecraft Versions
- Minecraft 1.8.8

## Features
- Fully customizable messages, countdowns and much more through a config file
- Statistics system with [MySQL](https://www.mysql.com/) (UUID based)
- Spectator mode and chat
- ... & basically every feature the game mode had on the [GommeHD.net](https://www.gommehd.net/) server

## Setup
- Drag the plugin jar file into your **plugins** folder
- Drag the **aura_world**, and the **lobby** folder into your server root folder
- Enter your MySQL server connection credentials in the **config.yml** file

## Permissions
Name | Description
--- | ---
`aura.start`| Allows the permission holder to utilize the start command
`aura.kick` | Allows the permission holder to kick a player out of the lobby phase and join himself

## Commands
Usage | Description
--- | ---
`/start`| Skips the pre game countdown
`/stats (<name>)` | Shows the players statistics

## Configuration

### Custom Maps
If you want to use your own lobby or ingame map please make sure you follow the steps below.

1. Set the following game rules in your world to false:
    - doDaylightCircle
    - doMobSpawning
    - doWeatherCircle
    - mobGriefing
2. Rename your world folder to
    - *aura_map* for the ingame map
    - *lobby* for the lobby map
3. Set the spawn point inside the *config.yaml* file
4. Drag the world into your server root folder

> Make sure your world is not a .zip file or any other archive, it has to be a folder

### Custom Items
Using custom items is more a lot difficult than using custom maps since this is the classic variant of Aura, 
even though it is still possible. 

First create a Base64 String containing an ArrayList of 9 items using the following method:
```kotlin
fun itemStackArrayListToBase64(items: ArrayList<ItemStack>): String {
   val outputStream = ByteArrayOutputStream()
   val dataOutput = BukkitObjectOutputStream(outputStream)

   dataOutput.writeInt(items.size)

   for (i in 0 until items.size)
       dataOutput.writeObject(items[i])

   dataOutput.close()

   return Base64Coder.encodeLines(outputStream.toByteArray())
}
```

Using the output of the method make an SQL request to the database. Replace the default inventory sorting, which can be
located in the *Inventories* table with the id *1*. For an example look at the query below:
```mysql
UPDATE Inventories
SET hotbar = "{value}"
WHERE id = 1;
```

### config.yml
Entry | Data Type | Description
--- | --- | ---
*min_players* | Int | Sets the minimum amount of players to start a game
*max_players* | Int | Sets the maximum amount of players (not spectators)
*hostname* | String | The hostname of the MySQL server
*port* | Int | The port of the MySQL server
*database* | String | The name of the database of the MySQL server
*username* | String | The username of the MySQL server user
*password* | String | The password of the MySQL server user
*waiting_delay* | Int | Sets the delay between waiting for players messages
*pre_game_countdown* | Int | Sets the countdown until the game starts
*protection_countdown* | Int | Sets the countdown until the protection phase ends
*ingame_countdown* | Int | Sets the countdown until the game ends
*tracker_countdown* | Int | Sets the countdown until the players get the tracker item
*post_game_countdown* | Int | Sets the countdown until the server restarts after the game
*inventory_sorting_name* | String | Sets the display name for the inventory sorting item
*inventory_sorting_name_inventory* | String | Sets the display name for the sorting inventory
*tracker_name* | String | Sets the display name for the tracker item
*tablist_header* | String | Sets the header of the tablist
*tablist_footer* | String | Sets the footer of the tablist
*remaining_on_quit* | Boolean | Shows how many players are remaining after a player disconnected
> Each countdown value is measured in seconds

### messages.yml
Entry | Data Type | Description | Variables
--- | --- | --- | ---
*join* | String | The message when a player joins the server | `{player}`
*quit* | String | The message when a player quits the server | `{player}`
*chat* | String | The message that is being displayed in the chat for everyone | `{player}`, `{message}`
*chat_spectator* | String | The message that is only being displayed in the spectator chat | `{player}`, `{message}`
*pre_game_timer* | String | The message when the pre game timer changes | `{time}`
*ingame_end_timer* | String | The message when the ingame end timer changes | `{time}`
*protection_timer* | String | The message when the protection phase timer changes | `{time}`
*post_game_timer* | String | The message when the post game timer changes | `{time}`
*waiting* | String | The "waiting for players" message | - 
*protection_start* | String | The message when the protection time begins | -
*protection_end* | String | The message when the protection time ends | -
*tracker_info* | String | The message when the players receive the tracker item | -
*ingame_end_info* | String | The message when the game is ending in 60 seconds | -
*death* | String | The message when a player died | `{player}`
*kill* | String | The message when a player got killed | `{player}`, `{killer}`
*remaining* | String | The message that shows how many players are still alive | `{count}`
*win* | String | The message when a player has won the game | `{player}`
*stats_header* | String | The message for the stats command output header | `{player}`
*stats_ranking* | String | The message for the stats command output ranking | `{rank}`
*stats_kills* | String | The message for the stats command output kills | `{kills}`
*stats_deaths* | String | The message for the stats command output deaths | `{deaths}`
*stats_kd* | String | The message for the stats command output K/D | `{kd}`
*stats_played* | String | The message for the stats command output played games | `{played}`
*stats_won* | String | The message for the stats command output won games | `{won}`
*stats_footer* | String | The message for the stats command output footer | -
*stats_not_found* | String | The message the statistics could not be found | -
*stats_invalid_rank* | String | The message if the user input contains an invalid rank | -

## Screenshots
![Inventory Sorting](screenshots/inventory_sorting.png "Inventory Sorting")

![Statistics](screenshots/statistics.png "Statistics")

## Contributing
When contributing to this repository, please first discuss the change you wish to make via issue, or any other method with the owner of this repository before making a change.

## Licence
This project is licensed under the MIT License - see the [LICENSE](/LICENSE) file for details