package io.yedox.imagine3d.world;

import com.sun.istack.internal.Nullable;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.utils.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WorldManager {
    public static void saveWorldToFile(World worldToSave, String path) {
        try {
            if(!Files.exists(Paths.get(path + worldToSave.getMetadata().getWorldName().replace("[^a-zA-Z0-9]+", "_")))) {
                new File(path + worldToSave.getMetadata().getWorldName().replace("[^a-zA-Z0-9]+", "_")).mkdir();
            }

            FileOutputStream fileOut = new FileOutputStream(path + worldToSave.getMetadata().getWorldName().replace("[^a-zA-Z0-9]+", "_") + "/" + worldToSave.getMetadata().getWorldName().replace("[^a-zA-Z0-9]+", "_").toLowerCase() + ".sav");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(worldToSave);
            objectOut.close();
            fileOut.close();

            Logger.logDebug("World saved successfully.");
        } catch (IOException ex) {
            Logger.logError("Unable to save world: " + ex);
            if (Game.developerDebugModeEnabled) ex.printStackTrace();
        }
    }

    @Nullable
    public static World loadWorldFromFile(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            // Read objects
            World readObject = (World) objectInputStream.readObject();

            objectInputStream.close();
            inputStream.close();

            return readObject;
        } catch (Exception ex) {
            Logger.logError("Unable to load world: " + ex);
            if (Game.developerDebugModeEnabled) ex.printStackTrace();
            return null;
        }
    }
}
