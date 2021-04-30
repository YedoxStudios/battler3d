-- Example I3Lua Script by Yeppii

-- This script shows yow how to change
-- the player's username using I3Lua

-- Import all the required packages
require 'i3lua.core.GameSettings'
require 'i3lua.utils.Logger'

-- GameSettings.setPlayerUsername() function will return
-- true if success else false

if GameSettings.setPlayerUsername("LuaDev") then
    Logger.logDebug("Your username has been changed to LuaDev")
else
    Logger.logDebug("Unable to change the username")
end 
