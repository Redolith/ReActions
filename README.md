ReActions
==========

ReActions bringing custom event processing system. It allows to check certain conditions and perform certain actions. If required conditions are not met, other actions will be carried out.

Terminology
-----------
For convenience, set of "event-condition-action" I will call [activator](http://dev.bukkit.org/bukkit-plugins/reactions/pages/main/activators/). Verifiable conditions are [flags](http://dev.bukkit.org/bukkit-plugins/reactions/pages/main/flags/). Actions that carried out in the case of successful validation flags are [actions](http://dev.bukkit.org/bukkit-plugins/reactions/pages/main/actions-and-reactions/). Actions that carried out in case of a negative test at least one flag - are [reactions](http://dev.bukkit.org/bukkit-plugins/reactions/pages/main/actions-and-reactions/). In addition you can use [placeholder](http://dev.bukkit.org/bukkit-plugins/reactions/pages/main/placeholders/) in actions, to determine dynamically calculated values.

Features
--------
* Ability to test lot of conditions (flags): permissions (or permisision groups), player balance or defined item in inventory... etc.
* Executing variuos actions: set a potion effect to player, teleport player to specified locations, executing command of other plugin.... etc.
* Linking activator to some kind of events: clicking button or walking at plate, enter or leave WorldGuard regions

What can I create using the ReActions?
--------------------------------------
Here is some examples:

* Teleporting system that can use payment (money or items) and depending on player permissions and in-game time. For example: teleporting only during daytime with one diamond block as payment.
* Simple admin-shop: to sell item (or group of items). For example you can sell a full set of dimond armour by single click of button.
* Creating an additional ways to solve quest or labirinthes. For example, player with dimond sword in hand will be teleported to a special locations, but player without dimond sword will be teleported for another place.
* Executing external plugin command will bring you a lot of fun. Check the video of ScLoad plugin: clicked buttons (that creates bridge and other structures) are activators.

Commands
--------
Main command of plugin is "/react" (Aliases: /rea, /ra)

* /react help — Help! I need somebody!
* /react add <type of activator> <id> [parameter] — create new activator. Type could be:button, plate, region, region_enter, region_leave, command, exec, pvp_kill, pvp_death, pvp_spawn, lever, door, join, mobclick. Some type of activator require an additional paramers (for example region name for activators region, region_enter, region_leave); some activators requires player to point block - just look on it while typing command (for example button, door, lever, etc..)
* /react add loc <location id> — create new location point. Id of this locations could be used at teleporting action (tp)
* /react add <activator id> f <flag> <parameters> — add new flag to activator
* /react add <activator id> a <action> <parameters> — add new action to activator
* /react add <activator id> a <action> <parameters> — add new reaction to activator
* /react copy [flag|actions|reactions] <source activator> <target activator> — copy flags, actions or reactions from one activator to another
* /react list [loc|group|type] [page] — display list of activators, groups and types.
* /react info <activator> [f|a|r] — display activator info (f,a,r - to show only flags, actions or reactions)
* /react group <activator> <groupname> — move activator to another (or new) group.
* /react remove [loc|activator] <id> — remove activator or location
* /react remove <activator id> <f|a|r> <num> — remove activator's flag, action or reaction with number <num> (numbers a shows with /react info command)
* /react clear <id> [f|a|r] — clear or flags, actions or reactions of activator
* /react debug [true|false|off] — debug mode: enable always true or always false for any flag
* /react check [radius] — find activators around you
* /react reload — reload config files

Permissions
-----------
* nreactions.config — main permission. For admins only :)
* reactions.debug — ability to use debug mode

Metrics and update checker
--------------------------
PlayEffect includes two features that use your server internet connection. First one is Metrics, using to collect [information about the plugin](http://mcstats.org/plugin/PlayEffect) (versions of plugin, of Java.. etc.) and second is update checker, checks new releases of plugin after PlayEffect startup and every half hour. This feature is using API provided by dev.bukkit.org. If you don't like this features you can easy disable it. To disable update checker you need to set parameter "version-check" to "false" in config.yml. Obtain more information about Metrics and learn how to switch off it, you can read [here](http://mcstats.org/learn-more/).