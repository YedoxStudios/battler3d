package io.yedox.imagine3d.world;

import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.AsyncUtils;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;

public class ThreadedWorldSaver {
    private Thread mainSaveThread;
    private boolean threadRunning;

    public ThreadedWorldSaver() {
        threadRunning = false;
        mainSaveThread = new Thread(() -> {
            while (threadRunning) {
                try {
                    WorldManager.saveWorldToFile(GUI.worldGenerator.getWorld(), "F:/Imagine3D/build/");
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    Logger.logDebug("Error while saving world: " + e.getMessage());
                } catch (NullPointerException e) {
                    // ignored
                }
            }
        });
    }

    public void start() {
        mainSaveThread.start();
        threadRunning = true;
    }
}
