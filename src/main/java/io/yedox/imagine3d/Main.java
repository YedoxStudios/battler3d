/**
 * MIT License
 *
 * Copyright (c) 2021 Yedox Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
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
import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.modapi.ModLoader;
import io.yedox.imagine3d.util.Logger;
import io.yedox.imagine3d.util.Utils;
import processing.core.PApplet;
import processing.data.JSONObject;
import processing.opengl.PGraphicsOpenGL;

import java.util.Arrays;

// Main game applet
public class Main extends PApplet {
    public static boolean showTitleMessage = false;
    public static ModLoader modLoader;
    private static String[] cargs;
    public boolean musicPlayed = false;
    public String titleMessage = "";

    public static void main(String[] args) {
        cargs = args;
        main(Main.class, cargs);
    }

    public void settings() {
        // Set mode to P3D
        size(856, 512, P3D);
        // Set antialiasing level to 4
        smooth(4);
    }

    public void setup() {
        try {
            // Set Resourcemanager applet
            Resources.setApplet(this);
            // Load version info before anything
            Game.loadResources(this);
            // Log client version
            Logger.logDebug(Utils.buildAppletTitle());
            // Log the command line args
            Logger.logDebug("Command line arguments: \"" + Arrays.toString(cargs) + "\"");
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

//            // Initialize modloader
//            modLoader = new ModLoader(this);
//            // Load the mods
//            modLoader.loadMods();
//            // Initialize the mods
//            modLoader.initMods();

        } catch (Exception exception) {
            // Print the exception
            Utils.printExceptionMessage(exception, this);
        }
    }

    public synchronized void draw() {
        // We add a try-catch block so that any exception will be caught
        try {
            // Do not allow player to move if chatbox visible
            GUI.getPlayer().setControllable(!GUI.chatBox.visible);

            // Uncomment this if you want to play music
            // if (Game.getCurrentScreen() == Game.Screen.MENU_SCREEN) {
            //     SoundRegistry.playMusic(SoundRegistry.Music.BIT2);
            //     Game.setSongsPlayed(Game.getSongsPlayed() + 1);
            // } else if (Game.getCurrentScreen() == Game.Screen.MAIN_GAME_SCREEN) {
            //     SoundRegistry.stopMusic(SoundRegistry.Music.BIT2);

            //     if (!SoundRegistry.isMusicPlaying(SoundRegistry.Music.BIT1)) {
            //         SoundRegistry.playMusic(SoundRegistry.Music.BIT3);
            //     }

            //     if (!SoundRegistry.isMusicPlaying(SoundRegistry.Music.BIT3)) {
            //         SoundRegistry.playMusic(SoundRegistry.Music.BIT1);
            //     }

            //     Game.setSongsPlayed(Game.getSongsPlayed() + 1);
            //     musicPlayed = !musicPlayed;
            // }

            if (Game.getCurrentScreen() == Game.Screen.MAIN_GAME_SCREEN && !GUI.getPlayer().isDead() && !GUI.chatBox.visible) {
                // Hide cursor
                getSurface().hideCursor();
            } else {
                // Show cursor
                getSurface().showCursor();
            }


            if (GUI.terrainManager.isTerrainGenerated())
                // Clear the screen with white
                GUI.clearScreen(this);
            // Draw the current screen
            GUI.draw(this);

            if (showTitleMessage)
                Utils.showTitle(titleMessage, this);
        } catch (Exception exception) {
            // Print the exception and terminate application
            Utils.printExceptionMessage(exception, this);
        }
    }

    public void keyPressed() {
        // Prevent closing when escape pressed
        if (keyCode == ESC) {
            key = '~';
            if (GUI.chatBox.visible) {
                GUI.chatBox.visible = false;
                GUI.getPlayer().setControllable(true);
            } else {
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
