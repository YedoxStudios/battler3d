package io.yedox.imagine3d.core;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import io.yedox.imagine3d.util.Logger;
import processing.core.PApplet;

import java.util.ArrayList;

public class SoundRegistry {
    public static ArrayList<AudioPlayer> sounds = new ArrayList<>();
    public static ArrayList<AudioPlayer> musics = new ArrayList<>();

    private static Minim minim;

    /*
     * NOTE: The sounds should be added in order of the
     * elements in the Sounds enum
     */
    public static void initSound(PApplet applet) {
        Logger.logDebug("Initializing sound library....");

        minim = new Minim(applet);

        sounds.add(minim.loadFile(Resources.getSoundPath("ui.click")));
        sounds.add(minim.loadFile(Resources.getSoundPath("player.death")));

        musics.add(minim.loadFile(Resources.getSoundPath("music.bit1")));
        musics.add(minim.loadFile(Resources.getSoundPath("music.bit2")));
        musics.add(minim.loadFile(Resources.getSoundPath("music.bit3")));
    }

    public static void playSound(Sounds sound, int pitch) {
        sounds.get(sound.ordinal()).rewind();
        sounds.get(sound.ordinal()).play(pitch);
    }

    public static void playSound(Sounds sound) {
        sounds.get(sound.ordinal()).rewind();
        sounds.get(sound.ordinal()).play();
    }

    public static void playMusic(Music music) {
        if(!musics.get(music.ordinal()).isPlaying()) {
            Logger.logDebug("Playing music '" + music.name() + "'");
            musics.get(music.ordinal()).rewind();
            musics.get(music.ordinal()).play();
        }
    }

    public static boolean isMusicPlaying(Music music) {
        return musics.get(music.ordinal()).isPlaying();
    }

    public static void stopMusic(Music music) {
        if(musics.get(music.ordinal()).isPlaying()) {
            musics.get(music.ordinal()).close();
        }
    }

    public enum Sounds {
        GUI_CLICK,
        PLAYER_DEATH
    }

    public enum Music {
        BIT1,
        BIT2,
        BIT3
    }
}
