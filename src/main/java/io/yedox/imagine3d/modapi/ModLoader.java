package io.yedox.imagine3d.modapi;

import io.yedox.imagine3d.util.Logger;
import processing.core.PApplet;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ModLoader {
    public static Class<BattlerMod> battlerModClass;
    private static boolean inititalized = false;
    private static PApplet pApplet;
    private File modJar;

    public ModLoader(PApplet applet) {
        ModLoader.pApplet = applet;
    }

    public void loadMods() {
        modJar = new File("F:\\Battler3D\\lib\\testmod\\out\\artifacts\\testmod_jar\\testmod.jar");
    }

    public static void invokeModMethod(String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if(inititalized) {
            Method method = battlerModClass.getDeclaredMethod(methodName, PApplet.class);
            BattlerMod instance = battlerModClass.newInstance();
            Object result = method.invoke(instance, pApplet);
        }
    }

    public void initMods() {
        try {
            // Init modloader
            URLClassLoader child = new URLClassLoader(
                    new URL[]{modJar.toURI().toURL()},
                    this.getClass().getClassLoader()
            );
            battlerModClass = (Class<BattlerMod>) Class.forName("battler3d.testmod.Main", true, child);
            Method method = battlerModClass.getDeclaredMethod("initMod", PApplet.class);

//            if (method.getAnnotation(RegisterClient.class) != null) {
//                Logger.logDebug("Loaded mod '" + battlerModClass.getAnnotation(RegisterClient.class).modName() + "'");
//            } else {
//                throw new InvalidClientInfoException();
//            }

            BattlerMod instance = battlerModClass.newInstance();
            Object result = method.invoke(instance, pApplet);
            inititalized = true;
        } catch (Exception exception) {
            Logger.logError("ModLoader Exception: " + exception);
        }
    }

    public static boolean isInititalized() {
        return inititalized;
    }
}
