-- Example I3Lua Script by Yeppii

-- This script shows yow how to change
-- the player's username using I3Lua

-- Import all the required packages
require 'i3lua.core.GameSettings'
require 'i3lua.utils.Logger'

-- Print 'It worked!' if username was
-- successfully changed, otherwise
-- print 'It didn't work :('

if GameSettings.setPlayerUsername("LuaDev") then
    Logger.logDebug("It worked!")
else
    Logger.logDebug("It didn't work :(")
end 
