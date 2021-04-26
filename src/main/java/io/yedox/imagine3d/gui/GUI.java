package io.yedox.imagine3d.gui;

import io.yedox.imagine3d.Main;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.core.GraphicsRenderer;
import io.yedox.imagine3d.core.Resources;
import io.yedox.imagine3d.entity.Player;
import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import io.yedox.imagine3d.mod_api.ModLoader;
import io.yedox.imagine3d.commands.scripting.ScriptParser;
import i3lua.LuaModElement;
import io.yedox.imagine3d.terrain.TerrainManager;
import io.yedox.imagine3d.utils.Logger;
import io.yedox.imagine3d.utils.ParticleSystem;
import io.yedox.imagine3d.utils.Utils;
import io.yedox.imagine3d.utils.animations.AnimationType;
import io.yedox.imagine3d.utils.animations.LinearAnimation;
import io.yedox.imagine3d.websocket.WebSocketClient;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PShader;

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

    public static boolean lightsEnabled;
    public static float cameraFov;
    public static LuaModElement luaModElement;
    public static LinearAnimation deathScreenFadeIn;

    private static GraphicsRenderer graphicsRenderer;
    private static PShader tileShader;
    private static PShader toonShader;
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
        terrainManager = new TerrainManager(20, 0, applet);

        // Init GraphicsRenderer
        graphicsRenderer = new GraphicsRenderer(applet);

        luaModElement = new LuaModElement("scripts/lua/test.lua", applet);

        // Init chatbox
        chatBox = new GUITextBox(applet, 10, applet.height - 50, applet.width - 30, 30) {
            @Override
            public void onValueEntered(String value) {
                super.onValueEntered(value);
                if(value.equals("/execlua")) luaModElement.execute();
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

        // Init shaders
        toonShader = applet.loadShader("shaders/toonshader/frag.glsl", "shaders/toonshader/vert.glsl");
        toonShader.set("fraction", 1.0f);

        tileShader = applet.loadShader("shaders/infinite_scroll.glsl");
        tileShader.set("resolution", (float) applet.width, (float) applet.height);
        tileShader.set("tileImage", backgrounds[0]);

        // Set font to F77 Minecraft
        applet.textFont(applet.createFont("fonts/Font.ttf", FontSize.NORMAL));

        // Initialize widgets
        initWidgets(applet);

        scriptParser = new ScriptParser("scripts/i3script/script.isc", applet);

        // NOTE: Script parser should be in another thread
        // because it uses the [delay] function
        Thread scriptThread = new Thread(() -> {
            try {
                scriptParser.parse(applet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "ScriptParserThread");

        // Start scripting thread
        scriptThread.start();

        // Setup variables
        lightsEnabled = Resources.getConfigValue(Boolean.class, "world.lightsEnabled");
        cameraFov = Float.parseFloat(Resources.getConfigValue(Double.class, "player.cameraFov").toString());

        // Init animations
        deathScreenFadeIn = new LinearAnimation(0, 90, 5, false, AnimationType.INCREMENT);
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

        // Settings button
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
                deathScreenFadeIn.reset();
                GUI.player.onPlayerRespawn(new PlayerRespawnEvent(player.position, player.health));
            }
        });

        // Game over label
        GUI.guiLabels.add(new GUILabel(applet, Resources.getResourceValue(String.class, "texts.label.game_over"), 263, 218, true, 200, 200, 0) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
                this.deathScreenWidget = true;
                this.fontSize = FontSize.LARGE;
                this.widgetId = 1;
            }

            @Override
            public void onDraw(PApplet sourceApplet) {
                super.onDraw(sourceApplet);
                this.x = (int) (applet.width / 2 - applet.textWidth(Resources.getResourceValue(String.class, "texts.label.game_over")) / 2);
            }
        });

        // Death cause label
        GUI.guiLabels.add(new GUILabel(applet, "death.cause", 263, 268, true, 255, 255, 255) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);
                this.deathScreenWidget = true;
                this.fontSize = FontSize.NORMAL;
                this.widgetId = 2;
            }

            @Override
            public void onDraw(PApplet sourceApplet) {
                this.setText(player.username + " " + player.deathCause + ".");
                this.x = sourceApplet.width / 2 - ((int) sourceApplet.textWidth(this.getText())) / 2;

                super.onDraw(sourceApplet);
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
        // Enable depth test
        applet.hint(PConstants.DISABLE_DEPTH_TEST);

        // Moving tiles shader
        tileShader.set("time", (float) (applet.millis() / 500.0));
        applet.shader(tileShader);

        // Draw background image
        applet.image(backgrounds[0], 0, 0, applet.width, applet.height);
        applet.resetShader();

        // Draw buttons
        guiButtons.stream().filter(button -> button.screen == Game.Screen.OPTIONS_SCREEN).forEach(button -> button.render(applet));
        // Draw labels
        guiLabels.stream().filter(label -> label.screen == Game.Screen.OPTIONS_SCREEN).forEach(label -> label.render(applet));

        // Disable depth test
        applet.hint(PConstants.ENABLE_DEPTH_TEST);
    }

    public static void drawMainMenu(PApplet applet) {
        try {
            // Moving tiles shader
            tileShader.set("time", (float) (applet.millis() / 500.0));
            applet.shader(tileShader);

            // Draw background image
            applet.image(backgrounds[0], 0, 0, applet.width, applet.height);
            applet.resetShader();

            // Draw logo
            applet.image(logo, (applet.width / 2) - (logo.pixelWidth / 2), 20);

            // Draw buttons
            guiButtons.stream().filter(button -> !button.hudWidget && !button.deathScreenWidget).forEach(button -> button.render(applet));

            // Draw labels
            guiLabels.stream().filter(label -> !label.hudWidget && !label.deathScreenWidget).forEach(label -> label.render(applet));

            // Invoke mod method
            ModLoader.invokeModMethod("mainMenuDraw");

            // Draw all GUIDialogBoxes
            guiDialogBoxes.forEach(guiDialogBox -> guiDialogBox.render(applet));

        } catch (Exception ignored) {}
    }

    public static void clearScreen(PApplet applet) {
        // Clear screen with light blue
        applet.background(131, 213, 245);
    }

    public static void onMousePressed(PApplet applet, int button) {
        if(applet.focused) {
            for (GUIButton guiButton : guiButtons) {
                guiButton.onMousePressed(applet);
            }
            player.onMousePress(applet, button);
        }
    }

    public static void onMouseReleased(PApplet applet) {
        if(applet.focused) {
            for (GUIButton guiButton : guiButtons) {
                guiButton.onMouseReleased(applet);
            }
        }
    }

    public static void onKeyPressed(PApplet applet) {
        if(applet.focused) {
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
    }

    public static void onKeyReleased(PApplet applet) {
        if(applet.focused) {
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
    }

    public static void drawGame(PApplet applet) {
        if (terrainManager.isTerrainGenerated()) {

            applet.pushMatrix();

            // Prevent culling
            applet.perspective(applet.PI / cameraFov, (float) applet.width / applet.height, 1, 1000000);

//            applet.shader(toonShader);

            if(lightsEnabled) applet.lights();

            player.update(applet);
            player.render();
            player.renderSkybox();

            applet.pointLight(200, 200, 200, applet.width/2, applet.height/2, -200);

            terrainManager.renderTerrain();

            // Reset fov
            applet.perspective(applet.PI / 3.0f, (float) applet.width / applet.height, 1, 1000000);

            applet.resetShader();

            if(lightsEnabled) applet.noLights();

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
            applet.image(hudImages[2], (applet.width / 2) - (hudImages[1].pixelWidth / 2), (applet.height / 2) - (hudImages[1].pixelHeight / 2));

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
            applet.text("Location: X: " + (int) player.position.x + " Y: " + (int) player.position.y + " Z: " + (int) player.position.z, 11, 61);
            applet.fill(255);
            applet.text("Location: X: " + (int) player.position.x + " Y: " + (int) player.position.y + " Z: " + (int) player.position.z, 10, 60);

            applet.fill(0);
            applet.text("Rotation: X: " + (int) player.tilt + " Y: " + (int) player.pan + " Z: " + 0, 11, 81);
            applet.fill(255);
            applet.text("Rotation: X: " + (int) player.tilt + " Y: " + (int) player.pan + " Z: " + 0, 10, 80);


            applet.fill(0);
            applet.text("Memory usage: " + Utils.bytesToMegabytes(Runtime.getRuntime().freeMemory()) + "/" + Utils.bytesToMegabytes(Runtime.getRuntime().totalMemory()) + " MB", 11, 101);
            applet.fill(255);
            applet.text("Memory usage: " + Utils.bytesToMegabytes(Runtime.getRuntime().freeMemory()) + "/" + Utils.bytesToMegabytes(Runtime.getRuntime().totalMemory()) + " MB", 10, 100);

            if (player.isDead()) {
                // Animate death screen
                deathScreenFadeIn.animate();

                // Set death cause
                guiLabels.forEach((label) -> {
                    if(label.widgetId == 2)
                        label.setText(player.username + " " + player.deathCause + ".");
                });

                applet.fill(150, 40, 40, deathScreenFadeIn.getValue());
                applet.rect(0, 0, applet.width, applet.height);
                applet.fill(255);

                applet.textSize(FontSize.NORMAL);

                if (deathScreenFadeIn.getValue() >= 90) {
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
                }
            } else {
                // Hide the widgets
                for (GUIButton button : guiButtons) {
                    if (button.deathScreenWidget)
                        button.visible = false;
                }
                for (GUILabel label : guiLabels) {
                    if (label.deathScreenWidget)
                        label.visible = false;
                }
            }

            if (Main.showTitleMessage)
                Utils.showTitle(Main.titleMessage, applet);

            applet.hint(PConstants.ENABLE_DEPTH_TEST);

            /* <-- END HUD --> */
        } else {
            tileShader.set("time", (float) (applet.millis() / 500.0));

            applet.shader(tileShader);
            // Draw background image
            applet.image(backgrounds[0], 0, 0, applet.width, applet.height);
            applet.resetShader();

            applet.noStroke();

            GUI.getGraphicsRenderer().drawShadowedText(Resources.getResourceValue(String.class, "texts.label.loading_screen_heading"), (applet.width / 2), (applet.height / 2), FontSize.MEDIUM, true);

            applet.fill(100);
            applet.rect((applet.width / 2) - (terrainManager.blockSize * 10 / 2), (applet.height / 2) + 20, terrainManager.blockSize * 10, 5);
            applet.fill(10, 200, 10);
            applet.rect((applet.width / 2) - (terrainManager.blockSize * 10 / 2), (applet.height / 2) + 20, terrainManager.generationProgress.x * 10, 5);
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
        public static final int NORMAL = 16;
        public static final int SMALL = 12;
        public static final int MEDIUM = 24;
        public static final int LARGE = 46;
    }
}
