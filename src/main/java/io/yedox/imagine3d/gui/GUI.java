package io.yedox.imagine3d.gui;

import io.yedox.imagine3d.Main;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.core.GraphicsRenderer;
import io.yedox.imagine3d.core.Resources;
import io.yedox.imagine3d.entity.Player;
import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import io.yedox.imagine3d.modapi.ModLoader;
import io.yedox.imagine3d.scripting.ScriptParser;
import io.yedox.imagine3d.terrain.TerrainManager;
import io.yedox.imagine3d.util.Logger;
import io.yedox.imagine3d.util.ParticleSystem;
import io.yedox.imagine3d.util.Utils;
import io.yedox.imagine3d.websocket.WebSocketClient;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PShader;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class GUI {
    public static ArrayList<GUIButton> guiButtons;
    public static ArrayList<GUILabel> guiLabels;
    public static GUITextBox chatBox;
    public static ArrayList<GUIDialogBox> guiDialogBoxes;

    public static PImage logo;
    public static PImage[] backgrounds = new PImage[10];
    public static PImage[] hudImages = new PImage[10];

    public static Player player;
    public static TerrainManager terrainManager;
    public static ParticleSystem particleSystem;
    public static Main main;
    public static WebSocketClient client;

    private static GraphicsRenderer graphicsRenderer;
    private static PShader tileShader;
    private static ScriptParser scriptParser;

    public static void init(PApplet applet) {
        Logger.logDebug("Initializing gui...");

        // Set framerate to 900
        applet.frameRate(900);

        // Set texture wrap to REPEAT
        applet.textureWrap(PConstants.REPEAT);

        // Init GUIButton array
        guiButtons = new ArrayList<>();
        // Init GUILabel array
        guiLabels = new ArrayList<>();
        // Init GUIDialogBox array list
        guiDialogBoxes = new ArrayList<>();

        // Init dialogboxes
        guiDialogBoxes.add(new GUIDialogBox(applet, new PVector(40, 40, 0), "text"));
        guiDialogBoxes.forEach(guiDialogBox -> guiDialogBox.centerDialog(applet));

        // Init logo
        logo = applet.loadImage("textures/gui/logo.png");
        // Init player and camera
        player = new Player(applet);

        // Initialize websocket client
        client = new WebSocketClient(applet);

        // Init terrain manager
        terrainManager = new TerrainManager(30, 0, applet);

        // Init GraphicsRenderer
        graphicsRenderer = new GraphicsRenderer(applet);

        // Init chatbox
        chatBox = new GUITextBox(applet, 10, applet.height - 50, applet.width - 30, 30) {
            @Override
            public void onValueEntered() {
                super.onValueEntered();
                client.sendMessage("{\"messageSend\": true, \"username\": \"" + GUI.player.username + "\", \"message\": \"" + getValue() + "\"}");
            }
        };
        chatBox.visible = false;

        // Init background images
        backgrounds[0] = applet.loadImage("textures/gui/backgrounds/bg1.png");

        // Init hud images
        hudImages[0] = applet.loadImage("textures/gui/heart.png");
        hudImages[1] = applet.loadImage("textures/gui/heart_bg.png");
        hudImages[2] = applet.loadImage("textures/gui/crosshair.png");

        // Init particle system
        particleSystem = new ParticleSystem(new PVector(5, -5, 5), applet);

        tileShader = applet.loadShader("shaders/infinite_scroll.glsl");
        tileShader.set("resolution", (float) applet.width, (float) applet.height);
        tileShader.set("tileImage", backgrounds[0]);

        // Set font to F77 Minecraft
        applet.textFont(applet.createFont("fonts/Font.ttf", FontSize.NORMAL));


        scriptParser = new ScriptParser("texts/script.b3s", applet);

        Thread scriptThread = new Thread(() -> {
            try {
                scriptParser.parse(applet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Initialize widgets
        initWidgets(applet);
    }

    private static void initWidgets(PApplet applet) {
        // Play button
        GUI.guiButtons.add(new GUIButton(applet) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
                // Setup button properties
                this.x = sourceApplet.width / 2 - this.width / 2;
                this.y = sourceApplet.height / 2 - (this.textureNormal.pixelHeight * 2 + 5);
            }

            @Override
            public void onAfterInit(PApplet applet) {
                super.onAfterInit(applet);
                this.label.setText(Resources.getResourceValue(String.class, "texts.button.play"));
            }

            @Override
            public synchronized void onClicked(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClicked(sourceButton, sourceApplet);
                this.visible = false;
                // Generate terrain if not generated
                if (!GUI.terrainManager.isTerrainGenerated()) {
                    GUI.terrainManager.start();
                }
                GUI.guiButtons.get(1).visible = false;
                GUI.guiButtons.get(2).visible = false;
                Game.setCurrentScreen(Game.Screen.MAIN_GAME_SCREEN);
            }
        });

        // Exit button
        GUI.guiButtons.add(new GUIButton(applet) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
                // Setup button properties
                this.x = sourceApplet.width / 2 - (this.width / 2);
                this.y = sourceApplet.height / 2 + (this.textureNormal.pixelHeight * 2 - 5);
            }

            @Override
            public void onAfterInit(PApplet applet) {
                super.onAfterInit(applet);
                this.label.setText(Resources.getResourceValue(String.class, "texts.button.settings"));
            }

            @Override
            public void onClicked(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClicked(sourceButton, sourceApplet);
                Game.setCurrentScreen(Game.Screen.OPTIONS_SCREEN);
            }
        });

        // Exit button
        GUI.guiButtons.add(new GUIButton(applet) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
                // Setup button properties
                this.x = sourceApplet.width / 2 - (this.width / 2);
                this.y = sourceApplet.height / 2 + (this.textureNormal.pixelHeight * 6 - 5);
            }

            @Override
            public void onAfterInit(PApplet applet) {
                super.onAfterInit(applet);
                this.label.setText(Resources.getResourceValue(String.class, "texts.button.exit"));
            }

            @Override
            public void onClicked(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClicked(sourceButton, sourceApplet);
                client.sendMessage("{\"disconnecting\": true, \"username\": \"" + player.username + "\"}");
                sourceApplet.exit();
            }
        });

        // Respawn button
        GUI.guiButtons.add(new GUIButton(applet) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
                // Setup button properties
                this.x = sourceApplet.width / 2 - (this.width / 2);
                this.y = sourceApplet.height / 2 + 60;
                this.deathScreenWidget = true;
            }

            @Override
            public void onAfterInit(PApplet applet) {
                super.onAfterInit(applet);
                this.label.setText(Resources.getResourceValue(String.class, "texts.button.play_again"));
                this.screen = Game.Screen.MAIN_GAME_SCREEN;
            }

            @Override
            public void onClicked(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClicked(sourceButton, sourceApplet);
                GUI.player.onPlayerRespawn(new PlayerRespawnEvent(player.position, player.dimensions, player.health));
            }
        });

        // Game over label
        GUI.guiLabels.add(new GUILabel(applet, Resources.getResourceValue(String.class, "texts.label.game_over"), 263, 218, true, 200, 200, 0) {
            boolean moveMode = false;

            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
//                this.x = sourceApplet.width / 2 - ((int) sourceApplet.textWidth(this.getText())) / 2;
                this.deathScreenWidget = true;
                this.fontSize = FontSize.LARGE;
            }

            @Override
            public void onAfterInit(PApplet applet) {
                super.onAfterInit(applet);
                this.x = 302;
                this.y = 212;
            }

            @Override
            public void onDraw(PApplet sourceApplet) {
                super.onDraw(sourceApplet);
                if (applet.keyPressed && applet.key == PConstants.CODED && applet.keyCode == PConstants.ALT) {
                    this.x = (int) (applet.mouseX - applet.textWidth(Resources.getResourceValue(String.class, "texts.label.game_over")) / 2);
                    this.y = applet.mouseY;
                    Logger.logDebug("x " + this.x + " y " + this.y);
                }
            }
        });

        // Death cause label
        GUI.guiLabels.add(new GUILabel(applet, "death.cause", 263, 268, true, 255, 255, 255) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
                this.deathScreenWidget = true;
                this.fontSize = FontSize.SMALL;
            }

            @Override
            public void onDraw(PApplet sourceApplet) {
                super.onDraw(sourceApplet);
                this.x = sourceApplet.width / 2 - ((int) sourceApplet.textWidth(this.getText())) / 2;
                this.setText(player.username + " " + player.deathCause + ".");
            }
        });

        guiButtons.add(new GUIButton(applet) {
            @Override
            public void onAfterInit(PApplet sourceApplet) {
                super.onAfterInit(sourceApplet);
                this.x = applet.width / 4 - this.width / 2;
                this.y = 20;
                this.label.setText("About Battler3D");
                this.screen = Game.Screen.OPTIONS_SCREEN;
            }

            @Override
            public void onClicked(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClicked(sourceButton, sourceApplet);
            }
        });

    }

    public static void draw(PApplet applet) {
        for (GUIButton button : GUI.guiButtons) {
            button.visible = button.screen == Game.Screen.MENU_SCREEN && Game.currentScreen == Game.Screen.MENU_SCREEN;
        }

        switch (Game.currentScreen) {
            case MENU_SCREEN:
                drawMainMenu(applet);
                break;
            case MAIN_GAME_SCREEN:
                drawGame(applet);
                break;
            case OPTIONS_SCREEN:
                drawOptionsScreen(applet);
                break;
        }

    }

    public static void drawOptionsScreen(PApplet applet) {
        applet.hint(PConstants.DISABLE_DEPTH_TEST);

        applet.textureMode(PConstants.REPEAT);
        applet.image(backgrounds[0], 0, 0, applet.width, applet.height);
        applet.textureMode(PConstants.NORMAL);

        // Draw buttons
        guiButtons.stream().filter(button -> button.screen == Game.Screen.OPTIONS_SCREEN).forEach(button -> button.render(applet));
        // Draw labels
        guiLabels.stream().filter(label -> label.screen == Game.Screen.OPTIONS_SCREEN).forEach(label -> label.render(applet));

        applet.hint(PConstants.ENABLE_DEPTH_TEST);
    }

    public static void drawMainMenu(PApplet applet) {
        try {
            tileShader.set("time", (float) (applet.millis() / 500.0));
            applet.shader(tileShader);
            // Draw background image
            applet.image(backgrounds[0], 0, 0, applet.width, applet.height);
            applet.resetShader();

            // Draw logo
            applet.image(logo, (applet.width >> 1) - (logo.pixelWidth >> 1), 50);

            // Draw buttons
            guiButtons.stream().filter(button -> !button.hudWidget && !button.deathScreenWidget).forEach(button -> button.render(applet));

            // Draw labels
            guiLabels.stream().filter(label -> !label.hudWidget && !label.deathScreenWidget).forEach(label -> label.render(applet));

            ModLoader.invokeModMethod("mainMenuDraw");

            guiDialogBoxes.forEach(guiDialogBox -> guiDialogBox.render(applet));

        } catch (Exception ignored) {

        }
    }

    public static void clearScreen(PApplet applet) {
        // Clear screen with light blue
        applet.background(131, 213, 245);
    }

    public static void onMousePressed(PApplet applet, int button) {
        for (GUIButton guiButton : guiButtons) {
            guiButton.onMousePressed(applet);
        }
        player.onMousePress(applet, button);
    }

    public static void onMouseReleased(PApplet applet) {
        for (GUIButton guiButton : guiButtons) {
            guiButton.onMouseReleased(applet);
        }
    }

    public static void onKeyPressed(PApplet applet) {
        for (int i = 0; i < Game.entityList.size(); i++) {
            Game.entityList.get(i).onKeyPressed(applet);
        }
        if ((applet.key == 't') && !chatBox.visible) {
            // Show chatbox
            chatBox.visible = true;
            // Reset value
            chatBox.setValue("");
        }

        chatBox.onKeyPress(applet);
    }

    public static void onKeyReleased(PApplet applet) {
        player.onKeyPress(applet);
        if (applet.key == 'r') {
            for (GUIButton button : guiButtons) {
                button.reloadResources(applet);
            }
            scriptParser.reload(applet);
            try {
                scriptParser.parse(applet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void drawGame(PApplet applet) {
        if (terrainManager.isTerrainGenerated()) {

            applet.pushMatrix();

            // Prevent culling
            applet.perspective(applet.PI / 3.0f, (float) applet.width / applet.height, 1, 1000000);

            applet.lights();
            applet.spotLight(2, -10, 2, 2, -10, 2, 1, 1, 1, 1, 1);

            player.update(applet);
            player.render();

            terrainManager.renderTerrain();

            applet.popMatrix();
            applet.hint(PConstants.DISABLE_DEPTH_TEST);

            /* <-- HUD --> */

            // Draw background health hearts
            for (int i = 0; i < player.maxHealth; i++) {
                applet.image(hudImages[1], (applet.width - 30) - i * (hudImages[0].pixelWidth + 1), 15);
            }

            // Draw health hearts
            for (int i = 0; i < player.health; i++) {
                applet.image(hudImages[0], (applet.width - 30) - i * (hudImages[0].pixelWidth + 1), 15);
            }

            // Draw crosshair
            applet.image(hudImages[2], (applet.width >> 1) - (hudImages[1].pixelWidth >> 1), (applet.height >> 1) - (hudImages[1].pixelHeight >> 1));

            // Draw HUD labels
            for (GUILabel label : guiLabels) {
                if (label.hudWidget)
                    label.render(applet);
            }

            chatBox.render();

            applet.textSize(FontSize.HUD_NORMAL);

            // I'm a bit lazy to add these in guiLabels list :P

            applet.fill(0);
            applet.text(Game.releaseName + " " + Game.releaseVersion, 11, 21);
            applet.fill(255);
            applet.text(Game.releaseName + " " + Game.releaseVersion, 10, 20);

            applet.fill(0);
            applet.text("FPS: " + ((int) applet.frameRate), 11, 41);
            applet.fill(255);
            applet.text("FPS: " + ((int) applet.frameRate), 10, 40);

            applet.fill(0);
            applet.text("Location: X: " + player.position.x + " Y: " + player.position.y + " Z: " + player.position.z, 11, 61);
            applet.fill(255);
            applet.text("Location: X: " + player.position.x + " Y: " + player.position.y + " Z: " + player.position.z, 10, 60);

            applet.fill(0);
            applet.text("Rotation: X: " + player.center.x + " Y: " + player.center.y + " Z: " + player.center.z, 11, 81);
            applet.fill(255);
            applet.text("Rotation: X: " + player.center.x + " Y: " + player.center.y + " Z: " + player.center.z, 10, 80);


            applet.fill(0);
            applet.text("Memory usage: " + Utils.bytesToMegabytes(Runtime.getRuntime().freeMemory()) + "/" + Utils.bytesToMegabytes(Runtime.getRuntime().totalMemory()) + " MB", 11, 101);
            applet.fill(255);
            applet.text("Memory usage: " + Utils.bytesToMegabytes(Runtime.getRuntime().freeMemory()) + "/" + Utils.bytesToMegabytes(Runtime.getRuntime().totalMemory()) + " MB", 10, 100);

            if (player.isDead()) {
                applet.fill(40, 90);
                applet.rect(0, 0, applet.width, applet.height);
                applet.fill(255);

                applet.textSize(FontSize.NORMAL);

                // Draw the widgets
                for (GUIButton button : guiButtons) {
                    if (button.deathScreenWidget) {
                        button.visible = true;
                        button.render(applet);
                    }
                }

                for (GUILabel label : guiLabels) {
                    if (label.deathScreenWidget) {
                        label.visible = true;
                        label.render(applet);
                    }
                }
            } else {
                // Draw the widgets
                for (GUIButton button : guiButtons) {
                    if (button.deathScreenWidget)
                        button.visible = false;
                }
                for (GUILabel label : guiLabels) {
                    if (label.deathScreenWidget)
                        label.visible = false;
                }
            }

            applet.hint(PConstants.ENABLE_DEPTH_TEST);

            /* <-- END HUD --> */
        } else {
            tileShader.set("time", (float) (applet.millis() / 500.0));

            applet.shader(tileShader);
            // Draw background image
            applet.image(backgrounds[0], 0, 0, applet.width, applet.height);
            applet.resetShader();

            applet.noStroke();

            GUI.getGraphicsRenderer().drawShadowedText(Resources.getResourceValue(String.class, "texts.label.loading_screen_heading"), (applet.width >> 1), (applet.height >> 1), FontSize.MEDIUM, true);

            applet.fill(10, 200, 10);
            applet.rect((applet.width >> 1) - 145, (applet.height >> 1) + 20, terrainManager.progress.x * 10, 5);
            applet.rect((applet.width >> 1) - 145, (applet.height >> 1) + 20, terrainManager.progress.x * 10, 5);
        }
    }

    public static GraphicsRenderer getGraphicsRenderer() {
        return graphicsRenderer;
    }

    public static Player getPlayer() {
        return player;
    }

    public static class FontSize {
        public static final int HUD_NORMAL = 10;
        public static final int NORMAL = 18;
        public static final int SMALL = 13;
        public static final int MEDIUM = 25;
        public static final int LARGE = 45;
    }
}
