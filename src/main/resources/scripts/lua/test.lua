require 'i3lua.core.GameSettings'
require 'i3lua.utils.Logger'

-- Try to set the username
if GameSettings:setPlayerUsername('why') then
    Logger:logDebug('yay it worked')
else
    Logger:logError('bruh')
end
