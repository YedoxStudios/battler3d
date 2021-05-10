-- Example I3Lua Script by Yeppii

-- Import all the required packages
require 'i3lua.core.game'
require 'i3lua.utils.logger'

--        Change player's username
-- game_settings.set_player_username() function
-- will return true if success else false
-- Syntax: set_player_username(username: String)
if game.set_player_username("LuaDev") then
    logger.debugln("Your username has been changed to LuaDev")
else
    logger.debugln("Unable to change the username")
end

--         Custom commands
-- You can add custom commands using
-- the Game.add_command() function
-- Syntax: add_command(commandName: String, commandResult: Function)
if game.add_command("testcommand", function()
    logger.debugln("Test command executed");
end)
