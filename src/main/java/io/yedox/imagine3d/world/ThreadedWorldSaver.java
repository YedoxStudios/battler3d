package io.yedox.imagine3d.world;

import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.Logger;

/**
 * Used for saving the world
 * automatically each minute
 */
public class ThreadedWorldSaver {
    private Thread mainSaveThread;
    private boolean threadRunning;

    public ThreadedWorldSaver() {
        threadRunning = false;
        mainSaveThread = new Thread(() -> {
            while (threadRunning) {
                try {
                    WorldManager.saveWorldToFile(GUI.worldGenerator.getWorld(), "./");
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    Logger.logDebug("Error while saving world: " + e.getMessage());
                } catch (NullPointerException e) {
                    // ignored
                }
            }
        });
    }

    /**
     * Starts the world saver thread
     */
    public void start() {
        mainSaveThread.start();
        threadRunning = true;
    }
}
