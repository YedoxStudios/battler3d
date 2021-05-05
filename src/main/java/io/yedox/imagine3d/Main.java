/**
 * MIT License
 * <p>
 * Copyright (c) 2021 Yedox Studios
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.yedox.imagine3d;

import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.core.Resources;
import io.yedox.imagine3d.core.SoundRegistry;
import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.mod_api.ModLoader;
import io.yedox.imagine3d.utils.ANSIConstants;
import io.yedox.imagine3d.utils.Logger;
import io.yedox.imagine3d.utils.Utils;
import io.yedox.imagine3d.utils.animations.AnimationType;
import io.yedox.imagine3d.utils.animations.LinearAnimation;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.opengl.PGraphicsOpenGL;

import java.util.Arrays;

// Main game applet
public class Main extends PApplet {
    public static boolean showTitleMessage = false;
    public static ModLoader modLoader;
    public static String titleMessage = "";

    // This contains the applet's arguments
    private static String[] appletConsoleArgs;

    // Loading screen image is stored in this variable
    private PImage loadingScreenImage;

    // Increase this to increase the time loading screen should stay open
    private int loadingScreenTimer = 500;

    private LinearAnimation loadingScreenFadeOut;

    public static void main(String[] args) {
        appletConsoleArgs = args;
        main(Main.class, appletConsoleArgs);
    }

    public void settings() {
        // Set mode to P3D
        size(856, 512, P3D);
        // Set antialiasing level to 8
        smooth(8);
    }

    public void setup() {
        try {
            // Set ResourceManager applet
            Resources.setApplet(this);

            // Set developer mode
            Game.developerDebugModeEnabled = Resources.getConfigValue(Boolean.class, "game.developerDebugMode");

            // Set splash screen visible
            Game.splashScreenEnabled = Resources.getConfigValue(Boolean.class, "game.splashScreenEnabled");

            // Developer mode should only be enabled for debugging purposes
            if (Game.developerDebugModeEnabled) {
                Logger.logDebug("NOTE: Developer debug mode is enabled, enable this only if you intend to make a module...", ANSIConstants.ANSI_YELLOW);
            }

            // Load splash image
            loadingScreenImage = loadImage("textures/gui/logo_yedox.png");

            // Load version info before anything
            Game.loadResources(this);
            // Log client version
            Logger.logDebug(Utils.buildAppletTitle());
            // Log the command line args
            Logger.logDebug("Command line arguments: \"" + Arrays.toString(appletConsoleArgs) + "\"");
            // Set texture sampling to NEAREST_NEIGHBOUR
            ((PGraphicsOpenGL) g).textureSampling(2);
            // Set texture mode to normal
            textureMode(NORMAL);
            // Set window title
            surface.setTitle(Game.releaseName + " " + Game.releaseVersion + (Game.isBeta() ? "-beta" : ""));
            // Set window size to 600x600
            surface.setSize(856, 512);
            // Set window position to 100,100
            surface.setLocation(displayWidth / 2 - (width / 2), displayHeight / 2 - (height / 2));

            // Init GUI elements and arrays
            GUI.init(this);
            // Initialize sounds
            SoundRegistry.initSound(this);
            // Initialize entities
            Game.initEntities(this);
            // Register commands
            GUI.registerCommands();
            // Init gameConfig values
            Game.initGameConfigValues();

            // Init animation
            loadingScreenFadeOut = new LinearAnimation(255, 0, 2f, false, AnimationType.DECREMENT);

        } catch (Exception exception) {
            // Print the exception
            Utils.printExceptionMessage(exception, this);
        }
    }

    public synchronized void draw() {
        // We add a try-catch block so that any exception will be caught
        try {
            // Draw only when window is focused
            if (focused) {
                // Do not allow player to move if chatbox visible
                GUI.getPlayer().setControllable(!GUI.chatBox.visible);

                if (Game.getCurrentScreen() == Game.Screen.MAIN_GAME_SCREEN && !GUI.getPlayer().isDead() && !GUI.chatBox.visible && GUI.worldGenerator.isTerrainGenerated() && !GUI.pauseScreen) {
                    // Hide cursor
                    getSurface().hideCursor();
                } else {
                    // Show cursor
                    getSurface().showCursor();
                }

                if (GUI.worldGenerator.isTerrainGenerated())
                    // Clear the screen with white
                    GUI.clearScreen(this);
                // Draw the current screen
                GUI.draw(this);
                // Draw splash screen
                drawSplashScreen();
                // reset tint
                tint(255, 255);
            }
        } catch (Exception exception) {
            // Print the exception and terminate application
            Utils.printExceptionMessage(exception, this);
        }
    }

    public void drawSplashScreen() {
        if (Game.splashScreenEnabled) {
            fill(60, 30, 179, loadingScreenFadeOut.getValue());
            rect(0, 0, width, height);
            tint(255, loadingScreenFadeOut.getValue());
            image(loadingScreenImage, width / 2 - loadingScreenImage.pixelWidth / 2, height / 2 - loadingScreenImage.pixelHeight / 2);
            if (loadingScreenTimer > 0) {
                loadingScreenTimer--;
            } else if (loadingScreenFadeOut.getValue() > 0 && loadingScreenTimer <= 0) {
                loadingScreenFadeOut.animate();
            }
            if (loadingScreenTimer <= 0 && loadingScreenFadeOut.getValue() <= 0) {
                Game.splashScreenEnabled = false;
            }
        }
    }

    public void keyPressed() {
        // Prevent closing when escape pressed
        if (keyCode == ESC) {
            key = '~';
            if (GUI.chatBox.visible) {
                GUI.chatBox.visible = false;
                GUI.getPlayer().setControllable(true);
            } else if (GUI.player.isDead()) {
                // Respawn player if dead
                GUI.player.onPlayerRespawn(new PlayerRespawnEvent(GUI.player.position, 8));
                // Reset death screen animation
                GUI.deathScreenFadeIn.reset();
            } else if (Game.getCurrentScreen() == Game.Screen.TERRAINGEN_SCREEN) {
                return;
            } else if (Game.getCurrentScreen() == Game.Screen.MAIN_GAME_SCREEN) {
                GUI.pauseScreen = true;
            } else if (GUI.pauseScreen) {
                Game.setCurrentScreen(Game.Screen.MAIN_GAME_SCREEN);
            } else {
                // Set current screen to Menu Screen
                Game.setCurrentScreen(Game.Screen.MENU_SCREEN);
            }
            textSize(GUI.FontSize.NORMAL);
        }
        // Invoke onKeyPressed()
        GUI.onKeyPressed(this);
    }

    public void keyReleased() {
        // Invoke onKeyReleased()
        GUI.onKeyReleased(this);
    }

    public void mousePressed() {
        // Invoke onMousePressed()
        GUI.onMousePressed(this, 0);
    }

    public void mouseReleased() {
        // Invoke onMouseReleased()
        GUI.onMouseReleased(this);
    }

    public void webSocketEvent(String message) {
        Logger.logDebug(message);
        JSONObject msg = parseJSONObject(message);
        if (msg.getBoolean("messageSend")) {
            showTitleMessage = true;
            titleMessage = msg.getString("message");
        }
    }
}
