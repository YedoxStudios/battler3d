/**
 * This file is a part of Battler3D
 *
 * @author Yeppii
 * @description A very small Minecraft-themed PvP game
 * LICENSE: MIT License (See LICENSE file)
 */

package io.yedox.imagine3d.utils;

import io.yedox.imagine3d.Main;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.gui.GUI;
import processing.core.PApplet;
import processing.core.PVector;

public class Utils {
    public static final long MEGABYTE = 1024L * 1024L;
    public static final long GIGABYTE = 1024L * 1024L * 1024L;
    private static final int DISTANCE_EPSILON = 1;
    private static final int ANGLE_EPSILON = 1;

    private static int alpha = 0;
    public static int bytesToMegabytes(long bytes) {
        return (int) (bytes / MEGABYTE);
    }

    public static boolean overRect(int x, int y, int width, int height, PApplet applet) {
        return applet.mouseX >= x && applet.mouseX <= x + width &&
                applet.mouseY >= y && applet.mouseY <= y + height;
    }

    public static void printExceptionMessage(Exception exception, PApplet applet) {
        // Print exception details to console
        Logger.setLogType("Error");
        Logger.logDebug("Oops! Exception! The exception that happened: " + exception);
        Logger.setLogType("Debug");
        exception.printStackTrace();
//        applet.exit();
    }

    private static float repeat(float t, float length) {
        return clamp(t - (float) Math.floor(t / length) * length, 0.0f, length);
    }

    public static float pingPong(float t, float length) {
        t = repeat(t, length * 2F);
        return length - Math.abs(t - length);
    }

    public static float clamp(float var1, float var2, float var3) {
        if (var1 > var3) {
            return var3;
        } else {
            return Math.max(var1, var2);
        }
    }
    
    public static void showTitle(String text, PApplet applet) {
        applet.push();
        applet.textSize(GUI.FontSize.LARGE * 2);
        applet.fill(10, 200, 10, alpha);
        applet.text(text, applet.width / 2 - applet.textWidth(text) / 2, (applet.height / 2) - 10);
        applet.textSize(GUI.FontSize.NORMAL);

        if(alpha < 255 && Main.showTitleMessage) {
            alpha++;
        }

        if (alpha == 255) {
            alpha = 0;
            Main.showTitleMessage = false;
        }

        applet.pop();
    }

    public static String buildAppletTitle() {
        return Game.releaseName + " " + Game.releaseVersion + (Game.isBeta() ? "-beta" : "");
    }

    public static PVector getRotationFacing(PVector from, PVector to) {
        PVector distance = new PVector(from.x, from.y, from.z).sub(to.x, to.y, to.z);


        //Don't actually need to call normalize for directionA - just doing it to indicate
        //that this vector must be normalized.
        final PVector directionA = new PVector(0, 1, 0).normalize();
        final PVector directionB = distance.copy().normalize();

        float rotationAngle = (float)Math.acos(directionA.dot(directionB));

        if (Math.abs(rotationAngle) < ANGLE_EPSILON)
        {
            //exit - don't do any rotation
            //angle is too small for rotation to be numerically stable
            return null;
        }

        //rotate object about rotationAxis by rotationAngle
        return directionA.copy().cross(directionB).normalize();
    }
}
