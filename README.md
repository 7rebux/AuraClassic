# Aura Classic Plugin
A minecraft plugin of the famous game mode **Aura Classic** made in pure [Kotlin](https://kotlinlang.org/) with the [Bukkit API](https://getbukkit.org/)

### Features
- Fully customizable messages, countdowns and much more through a config file
- Statistics system with [MySQL](https://www.mysql.com/) (UUID based)
- Spectator mode and chat
- ... & basically every feature the game mode had on the [GommeHD.net](https://www.gommehd.net/) server

### Setup
Just drag the plugin jar file into your **plugins** folder, and you're ready to play

### Permissions
Name | Description
--- | ---
`aura.start`| Allows the permission holder to issue the start command
`aura.kick` | Allows the permission holder to kick a player out of the lobby phase and join himself
`aura.setspawn` | Allows the permission holder to set the spawn location on the lobby and the game map

### Commands
Name | Description
--- | ---
`/start`| Skips the pre game countdown
`/setspawn <lobby/map>` | Sets the spawn location for all players 
`/stats <name>` | Shows the players statistics of the last month
`/statsd <days> <name>` | Shows the players statistics of the last `days` days
`/statsall <name>` | Shows the players statistics of all time

### Config
> !!! Since it is the **Classic** variant of Aura, you are not able to change the items and other base game mechanics !!!
#### main.yml
Entry | Data Type | Description
--- | --- | ---
*min_players* | Int | Sets the **minimum** amount of players to start a game
*max_players* | Int | Sets the **maximum** amount of players (not spectators)
*waiting_countdown* | Int | Sets the **delay** between **waiting for players** messages
*pre_game_countdown* | Int | Sets the countdown until the game starts
*protection_countdown* | Int | Sets the countdown until the protection phase ends
*ingame_countdown* | Int | Sets the countdown until the game ends
*tracker_countdown* | Int | Sets the countdown until the players get the **tracker** item
*post_game_countdown* | Int | Sets the countdown until the server restarts after the game
> Each countdown value is measured in seconds

#### messages.yml
Entry | Data Type | Description | Variables
--- | --- | --- | ---
*join* | String | The message when a player joins the server | `{player}`
*quit* | String | The message when a player quits the server | `{player}`
*pre_game_timer* | String | The message when the pre game timer changes | `{time}`
*ingame_end_timer* | String | The message when the ingame end timer changes | `{time}`
*protection_timer* | String | The message when the protection phase timer changes | `{time}`
*post_game_timer* | String | The message when the post game timer changes | `{time}`
*waiting* | String | The **waiting for players** message | - 
*protection_start* | String | The message when the protection time begins | -
*protection_end* | String | The message when the protection time ends | -
*tracker_info* | String | The message when the players receive the **tracker** item | -
*ingame_end_info* | String | The message when the game is ending in **60 seconds** | -
*death* | String | The message when a player died | `{player}`
*kill* | String | The message when a player got killed | `{player}`, `{killer}`
*remaining* | String | The message that shows how many players are still alive | `{count}`
*win* | String | The message when a player has won the game | `{player}`

### Screenshots
![Inventory Sorting](screenshots/inventory_sorting.png "Inventory Sorting")

![Statistics](screenshots/statistics.png "Statistics")

### Contributing
When contributing to this repository, please first discuss the change you wish to make via issue, or any other method with the owner of this repository before making a change.

### Licence
This project is licensed under the MIT License - see the [LICENSE](/LICENSE) file for details