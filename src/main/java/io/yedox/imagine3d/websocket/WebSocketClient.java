package io.yedox.imagine3d.websocket;

import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.core.Resources;
import processing.core.PApplet;
import websockets.WebsocketClient;

public class WebSocketClient {
    private WebsocketClient client;

    public WebSocketClient(PApplet applet) {
        if (Game.multiplayerEnabled) {
            try {
                // Init websocket client
                client = new WebsocketClient(applet, Resources.getConfigValue(String.class, "multiplayer.server.ip"));
            } catch (Exception ignored) {
                // Just ignore bruh
            }
        }
    }

    public void sendMessage(String message) {
        // DO NOT TRY SEND MESSAGE IF MULTIPLAYER IS DISABLED OR GAME CRASH DONT DO IT
        if (Game.multiplayerEnabled) {
            if (message == null) {
                throw new NullPointerException("Ayo you're trying to send the server an empty message! Maybe caused by a buggy module?");
            }
            client.sendMessage(message);
        }
    }
}
