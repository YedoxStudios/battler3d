-- Example I3Lua Script by Yeppii

-- Import all the required packages
require 'i3lua.core.Game'
require 'i3lua.utils.Logger'

--        Change player's username
-- Game_settings.set_player_username() function
-- will return true if success else false
-- Syntax: set_player_username(username: String)
if Game.set_player_username("LuaDev") then
    Logger.debugln("Your username has been changed to LuaDev")
else
    Logger.debugln("Unable to change the username")
end

--         Custom commands
-- You can add custom commands using
-- the Game.add_command() function
-- Syntax: add_command(commandName: String, commandResult: Function)
if Game.add_command("testcommand", function()
    Logger.debugln("Test command executed");
end)
