package io.yedox.imagine3d.gui;

import com.jogamp.opengl.GL;
import i3lua.LuaScriptingEngine;
import io.yedox.imagine3d.Main;
import io.yedox.imagine3d.commands.CommandBuilder;
import io.yedox.imagine3d.commands.CommandManager;
import io.yedox.imagine3d.commands.MissingArgumentException;
import io.yedox.imagine3d.commands.scripting.ScriptParser;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.core.GraphicsRenderer;
import io.yedox.imagine3d.core.Resources;
import io.yedox.imagine3d.entity.Player;
import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import io.yedox.imagine3d.mod_api.ModLoader;
import io.yedox.imagine3d.utils.Logger;
import io.yedox.imagine3d.utils.ParticleSystem;
import io.yedox.imagine3d.utils.Utils;
import io.yedox.imagine3d.utils.animations.AnimationType;
import io.yedox.imagine3d.utils.animations.LinearAnimation;
import io.yedox.imagine3d.websocket.WebSocketClient;
import io.yedox.imagine3d.world.ThreadedWorldSaver;
import io.yedox.imagine3d.world.WorldGenerator;
import io.yedox.imagine3d.world.WorldManager;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGL;
import processing.opengl.PShader;

import java.util.ArrayList;
import java.util.Objects;

public class GUI {
    public static ArrayList<GUIButton> guiButtons;
    public static ArrayList<GUILabel> guiLabels;
    public static ArrayList<GUIDialogBox> guiDialogBoxes;
    public static GUITextBox chatBox;

    public static PImage logo;
    public static PImage[] backgrounds = new PImage[10];
    public static PImage[] hudImages = new PImage[10];

    public static Player player;
    public static WorldGenerator worldGenerator;
    public static ParticleSystem particleSystem;
    public static Main main;
    public static WebSocketClient mpClient;
    public static LuaScriptingEngine luaModElement;

    public static boolean lightsEnabled;
    public static boolean torchEnabled;
    public static boolean zoomingOut;
    public static boolean pauseScreen;
    public static boolean screenBlurEnabled;

    public static float cameraFov;
    public static float cameraFovStatic;

    public static LinearAnimation deathScreenFadeIn;
    public static LinearAnimation fovZoomIn;
    public static LinearAnimation fovZoomOut;

    private static GraphicsRenderer graphicsRenderer;
    private static PShader tileShader;
    private static PShader blurShader;
    private static ScriptParser scriptParser;
    private static ThreadedWorldSaver worldSaver;

    public static void init(PApplet applet) {
        Logger.logDebug("Initializing GUI...");

        // Set framerate to 900
        applet.frameRate(900);
        // Set texture wrap to REPEAT
        applet.textureWrap(PConstants.REPEAT);
        // Enable mipmaps
        applet.hint(PConstants.ENABLE_TEXTURE_MIPMAPS);
        // Enable stroke perspective
        applet.hint(PConstants.ENABLE_STROKE_PERSPECTIVE);

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

        // Initialize websocket client
        mpClient = new WebSocketClient(applet);

        // Init player and camera
        player = new Player(applet);

        // Init world generator
        worldGenerator = new WorldGenerator(20, applet);

        // Init world saver
        worldSaver = new ThreadedWorldSaver();

        // Init GraphicsRenderer
        graphicsRenderer = new GraphicsRenderer(applet);

        // Init chatbox
        chatBox = new GUITextBox(applet, 10, applet.height - 42, applet.width - 30, 21);

        // Send message to server/parse command
        chatBox.addInputListener((value, textbox, pApplet) -> {
            CommandManager.parse(value, pApplet);
            mpClient.sendMessage("{\"messageSend\": true, \"username\": \"" + GUI.player.username + "\", \"message\": \"" + textbox.getValue() + "\"}");
        });

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
        blurShader = applet.loadShader("shaders/blur.glsl");
        tileShader.set("resolution", (float) applet.width, (float) applet.height);
        tileShader.set("tileImage", backgrounds[0]);

        // Set font to F77 Minecraft
        applet.textFont(applet.createFont("fonts/Font.ttf", FontSize.NORMAL));

        // Initialize widgets
        initWidgets(applet);

        // Init script parser
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
        torchEnabled = Resources.getConfigValue(Boolean.class, "player.isHoldingTorch");
        screenBlurEnabled = Resources.getConfigValue(Boolean.class, "game.screenBackgroundBlur");
        cameraFov = Float.parseFloat(Resources.getConfigValue(Double.class, "player.cameraFov").toString());
        cameraFovStatic = Float.parseFloat(Resources.getConfigValue(Double.class, "player.cameraFov").toString());
        zoomingOut = false;
        pauseScreen = false;

        // Init animations
        deathScreenFadeIn = new LinearAnimation(0f, 90f, 5, false, AnimationType.INCREMENT);
        fovZoomIn = new LinearAnimation(cameraFovStatic, cameraFovStatic + 10f, 0.5f, false, AnimationType.INCREMENT);
        fovZoomOut = new LinearAnimation(cameraFovStatic + 10f, cameraFovStatic, 0.5f, false, AnimationType.DECREMENT);
    }

    public static void registerCommands() {
        Logger.logDebug("Registering commands...");

        // Execute lua command
        CommandManager.addCommand(CommandBuilder.createCommand("execlua").executes((appletCtx, args1) -> {
            if (CommandManager.checkArg(args1, 0)) {
                GUI.luaModElement = new LuaScriptingEngine(args1[0], appletCtx);
                GUI.luaModElement.execute();
            } else {
                throw new MissingArgumentException(0);
            }
        }));

        // Kill player command
        CommandManager.addCommand(CommandBuilder.createCommand("kill").executes((appletCtx, args) -> {
            getPlayer().hurt(getPlayer().health, "was killed using commands");
        }));

        // Game config command
        CommandManager.addCommand(CommandBuilder.createCommand("gameconfig").executes((appletCtx, args) -> {
            if (CommandManager.checkArg(args, 0)) {
                if ("set".equals(args[0])) {
                    if (CommandManager.checkArg(args, 1)) {
                        Game.gameConfigValues.forEach((key, value) -> {
                            if (Objects.equals(key, args[1])) {
                                if (CommandManager.checkArg(args, 2)) {
                                    Game.gameConfigValues.put(key, args[2]);
                                    Logger.logDebug("GameConfig value '" + key + "' is now set to '" + args[2] + "'. Type /gameconfig list to print all GameConfig values.");
                                }
                            }
                        });
                    }
                } else if ("list".equals(args[0])) {
                    Logger.logDebug(Game.gameConfigValues.toString());
                }
            }
        }));

        CommandManager.addCommand(CommandBuilder.createCommand("world").executes((appletCtx, args) -> {
            if (CommandManager.checkArg(0, args).equals("save")) {
                WorldManager.saveWorldToFile(worldGenerator.getWorld(), "F:/Imagine3D/build/");
            } else if (CommandManager.checkArg(0, args).equals("load")) {
                if (CommandManager.checkArg(args, 1)) {
                    worldGenerator.loadWorld(Objects.requireNonNull(WorldManager.loadWorldFromFile(args[1])), appletCtx);
                }
            }
        }));

        CommandManager.addCommand(CommandBuilder.createCommand("offset").executes((appletCtx, args) -> {
            if (CommandManager.checkArg(args, 0) && CommandManager.checkArg(args, 1))
                worldGenerator.offsetBlocks(Float.parseFloat(CommandManager.checkArg(0, args)), Float.parseFloat(CommandManager.checkArg(1, args)));
            else
                throw new MissingArgumentException(1, "Syntax for command offset: '/offset <min> <max>'");
        }));

        CommandManager.addCommand(CommandBuilder.createCommand("setblock").executes((appletCtx, args) -> {
            if (CommandManager.checkArg(args, 0) && CommandManager.checkArg(args, 1) && CommandManager.checkArg(args, 2))
                worldGenerator.getBlockAt(new PVector(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))).destroy();
            else
                throw new MissingArgumentException(1, "Syntax for command setblock: '/setblock <blockid>>'");
        }));
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
            public synchronized void onClick(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClick(sourceButton, sourceApplet);
                this.visible = false;

                // Set game screen to TERRAINGEN_SCREEN
                Game.setCurrentScreen(Game.Screen.TERRAINGEN_SCREEN);

                // Generate terrain if not generated
                if (!GUI.worldGenerator.isTerrainGenerated()) {
                    GUI.worldGenerator.start();
                    worldSaver.start();
                }

                GUI.player = new Player(applet);

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
            public void onClick(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClick(sourceButton, sourceApplet);
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
            public void onClick(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClick(sourceButton, sourceApplet);
                mpClient.sendMessage("{\"disconnecting\": true, \"username\": \"" + player.username + "\"}");
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
            public void onClick(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClick(sourceButton, sourceApplet);
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

        // Resume button
        GUI.guiButtons.add(new GUIButton(applet) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);

                // Setup button properties
                this.x = sourceApplet.width / 2 - (this.width / 2);
                this.y = sourceApplet.height / 2 - 30;
                this.pauseScreenWidget = true;
            }

            @Override
            public void onAfterInit(PApplet applet) {
                super.onAfterInit(applet);

                this.label.setText(Resources.getResourceValue(String.class, "texts.button.resume"));
                this.screen = Game.Screen.PAUSE_SCREEN;
            }

            @Override
            public void onClick(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClick(sourceButton, sourceApplet);
                Game.setCurrentScreen(Game.Screen.MAIN_GAME_SCREEN);
                GUI.pauseScreen = false;
            }
        });

        // Exit to menu button
        GUI.guiButtons.add(new GUIButton(applet) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);

                // Setup button properties
                this.x = sourceApplet.width / 2 - (this.width / 2);
                this.y = sourceApplet.height / 2 - (30 - (this.height + 10));
                this.pauseScreenWidget = true;
            }

            @Override
            public void onAfterInit(PApplet applet) {
                super.onAfterInit(applet);

                this.label.setText(Resources.getResourceValue(String.class, "texts.button.exit_to_menu"));
                this.screen = Game.Screen.PAUSE_SCREEN;
            }

            @Override
            public void onClick(GUIButton sourceButton, PApplet sourceApplet) {
                super.onClick(sourceButton, sourceApplet);
                Game.setCurrentScreen(Game.Screen.MENU_SCREEN);
                GUI.pauseScreen = false;
            }
        });

        // Pause Screen label
        GUI.guiLabels.add(new GUILabel(applet, Resources.getResourceValue(String.class, "texts.label.pause_screen_label"), 0, 120, true, 255, 255, 255) {
            @Override
            public void onInit(PApplet sourceApplet) {
                super.onInit(sourceApplet);

                this.x = sourceApplet.width / 2 - (int) (sourceApplet.textWidth(this.getText()) / 2);
                this.pauseScreenWidget = true;
                this.screen =  Game.Screen.PAUSE_SCREEN;
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
            case OPTIONS_SCREEN:
                drawOptionsScreen(applet);
                break;
            case TERRAINGEN_SCREEN:
                drawTerrainGenScreen(applet);
                break;
            case MAIN_GAME_SCREEN:
                drawGame(applet);
                break;
        }
    }

    public static void drawOptionsScreen(PApplet applet) {
        // Disable depth test
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

        // Enable depth test
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
            guiButtons.stream().filter(button -> !button.hudWidget && !button.deathScreenWidget && !button.pauseScreenWidget).forEach(button -> button.render(applet));

            // Draw labels
            guiLabels.stream().filter(label -> !label.hudWidget && !label.deathScreenWidget && !label.pauseScreenWidget).forEach(label -> label.render(applet));

            // Invoke mod method
            ModLoader.invokeModMethod("mainMenuDraw");

            // Draw all GUIDialogBoxes
            guiDialogBoxes.forEach(guiDialogBox -> guiDialogBox.render(applet));

        } catch (Exception ignored) {
        }
    }

    public static void clearScreen(PApplet applet) {
        // Clear screen with light blue
        applet.background(131, 213, 245);
    }

    public static void onMousePressed(PApplet applet, int button) {
        if (applet.focused) {
            for (GUIButton guiButton : guiButtons) {
                guiButton.onMousePressed(applet);
            }
            player.onMousePress(applet, button);
        }
    }

    public static void onMouseReleased(PApplet applet) {
        if (applet.focused) {
            for (GUIButton guiButton : guiButtons) {
                guiButton.onMouseReleased(applet);
            }
        }
    }

    public static void onKeyPressed(PApplet applet) {
        if (applet.focused) {
            for (int i = 0; i < Game.entityList.size(); i++) {
                Game.entityList.get(i).onKeyPressed(applet);
            }
            if ((applet.key == 't' || applet.key == '/') && !chatBox.visible) {
                // Show chatbox
                chatBox.visible = true;
                // Reset value
                chatBox.setValue("");
            }
            chatBox.onKeyPress(applet);
        }
    }

    public static void onKeyReleased(PApplet applet) {
        if (applet.focused) {
            player.onKeyPress(applet);
            if (applet.key == 'r' && !chatBox.visible) {
                for (GUIButton button : guiButtons) {
                    button.reloadResources(applet);
                }
                scriptParser.reload(applet);
                try {
                    scriptParser.parse(applet);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (applet.key == 'l' && !chatBox.visible) {
                torchEnabled = !torchEnabled;
            }

            if (applet.key == PConstants.CODED && applet.keyCode == PConstants.SHIFT && !chatBox.visible) {
                fovZoomIn.reset();
                zoomingOut = true;
                player.resetSensitivity();
            }
        }
    }

    public static void drawGame(PApplet applet) {
        if (worldGenerator.isTerrainGenerated()) {
            if (applet.keyPressed && applet.key == PConstants.CODED && applet.keyCode == PConstants.SHIFT && !chatBox.visible) {
                fovZoomIn.animate();
                GUI.cameraFov = fovZoomIn.getValue();
                player.sensitivity = 0.1f;
            }

            if (zoomingOut) {
                fovZoomOut.animate();
                GUI.cameraFov = fovZoomOut.getValue();
                if (GUI.cameraFov <= GUI.cameraFovStatic) {
                    fovZoomOut.reset();
                    zoomingOut = false;
                }
            }

            applet.pushMatrix();

            // Prevent culling
            applet.perspective(applet.PI / cameraFov, (float) applet.width / applet.height, 1, 1000000);

            if (lightsEnabled) applet.lights();

            player.update(applet);
            player.render();
            player.renderSkybox();

            if (!torchEnabled)
                applet.pointLight(100, 100, 100, player.position.x, player.position.y - 1, player.position.z);

            worldGenerator.renderTerrain();

            // Reset fov
            applet.perspective(applet.PI / 3.0f, (float) applet.width / applet.height, 1, 1000000);

            applet.resetShader();

            if (lightsEnabled) applet.noLights();

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
            applet.text("Location: X: " + (int) player.blockPosition.x + " Y: " + (int) player.blockPosition.y + " Z: " + (int) player.blockPosition.z, 11, 61);
            applet.fill(255);
            applet.text("Location: X: " + (int) player.blockPosition.x + " Y: " + (int) player.blockPosition.y + " Z: " + (int) player.blockPosition.z, 10, 60);

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
                guiLabels.stream().filter(label -> label.widgetId == 2).forEach(label -> label.setText(player.username + " " + player.deathCause + "."));

                applet.fill(150, 40, 40, deathScreenFadeIn.getValue());
                applet.rect(0, 0, applet.width, applet.height);
                applet.fill(255);

                applet.textSize(FontSize.NORMAL);

                if (deathScreenFadeIn.getValue() >= 90) {
                    if(screenBlurEnabled) {
                        applet.filter(blurShader);
                        applet.fill(255, 0);
                        applet.rect(0, 0, applet.width, applet.height);
                        applet.resetShader();
                    }

                    // Draw the widgets
                    guiButtons.stream().filter(button -> button.deathScreenWidget).forEach(button -> {
                        button.visible = true;
                        button.render(applet);
                    });

                    guiLabels.stream().filter(label -> label.deathScreenWidget).forEach(label -> {
                        label.visible = true;
                        label.render(applet);
                    });
                }
            } else {
                // Hide the widgets
                guiButtons.stream().filter(button -> button.deathScreenWidget).forEach(button -> button.visible = false);
                guiLabels.stream().filter(label -> label.deathScreenWidget).forEach(label -> label.visible = false);
            }

            if (Main.showTitleMessage)
                Utils.showTitle(Main.titleMessage, applet);

            if (pauseScreen) {
                if(screenBlurEnabled) {
                    applet.filter(blurShader);
                    applet.fill(255, 0);
                    applet.rect(0, 0, applet.width, applet.height);
                    applet.resetShader();
                }

                // Draw buttons
                guiButtons.stream().filter(guiButton -> guiButton.pauseScreenWidget).forEach(guiButton -> {
                    guiButton.visible = true;
                    guiButton.render(applet);
                });

                // Draw labels
                guiLabels.stream().filter(guiLabel -> guiLabel.pauseScreenWidget).forEach(label -> {
                    label.visible = true;
                    label.render(applet);
                });
            } else {
                // Hide the widgets
                guiButtons.stream().filter(guiButton -> guiButton.pauseScreenWidget).forEach(guiButton -> guiButton.visible = false);
                guiLabels.stream().filter(guiLabel -> guiLabel.pauseScreenWidget).forEach(label -> label.visible = false);
            }

            applet.hint(PConstants.ENABLE_DEPTH_TEST);

            /* <-- END HUD --> */
        } else {
            drawTerrainGenScreen(applet);
        }
    }

    public static void drawTerrainGenScreen(PApplet applet) {
        tileShader.set("time", (float) (applet.millis() / 500.0));

        applet.shader(tileShader);
        // Draw background image
        applet.image(backgrounds[0], 0, 0, applet.width, applet.height);
        applet.resetShader();

        applet.noStroke();

        GUI.getGraphicsRenderer().drawShadowedText(Resources.getResourceValue(String.class, "texts.label.loading_screen_heading"), (applet.width / 2), (applet.height / 2), FontSize.MEDIUM, true);

        applet.fill(100);
        applet.rect((applet.width / 2) - (worldGenerator.blockSize * 10 / 2), (applet.height / 2) + 20, worldGenerator.blockSize * 10, 5);
        applet.fill(10, 200, 10);
        applet.rect((applet.width / 2) - (worldGenerator.blockSize * 10 / 2), (applet.height / 2) + 20, worldGenerator.generationProgress.x * 10, 5);
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
