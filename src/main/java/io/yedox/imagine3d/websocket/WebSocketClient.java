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

            }
        }
    }

    public void sendMessage(String message) {
        if (Game.multiplayerEnabled) {
            if (message == null) {
                throw new NullPointerException("Message should not be null");
            }
            client.sendMessage(message);
        }
    }
}
