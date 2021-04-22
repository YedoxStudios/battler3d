package io.yedox.imagine3d.commands.scripting;

import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;

public class ScriptParser {
    private String[] _script = {};
    private PApplet applet;
    private String fn;

    public ScriptParser(String filename, PApplet applet) {
        this._script = applet.loadStrings(filename);
        this.applet = applet;
        this.fn = filename;
    }

    public ScriptParser(String[] input, PApplet applet) {
        this._script = input;
        this.applet = applet;
    }

    public void reload(PApplet applet) {
        this._script = applet.loadStrings(fn);
    }

    // A very bad script parser
    public void parse(PApplet pApplet) throws InterruptedException {
        for (int i = 0, scriptLength = _script.length; i < scriptLength; i++) {
            String[] sp = _script[i].split(" ");
            if (sp[0].equals("[windowTitle]")) {
                if (sp[1] != null) {
                    if (sp[1].equals("title:")) {
                        if (sp[2] != null) {
                            pApplet.getSurface().setTitle(sp[2].replace('_', ' '));
                        }
                    } else {
                        Logger.logDebug("Error: windowTitle requires an argument 'title'.");
                    }
                }
            } else if (sp[0].equals("[print]")) {
                if (sp[1] != null) {
                    if (sp[1].equals("msg:")) {
                        if (sp[2] != null) {
                            System.out.println(sp[2].replace('_', ' '));
                        }
                    } else {
                        Logger.logDebug("Error: windowTitle requires an argument 'title'.");
                    }
                }
            } else if (sp[0].equals("[delay]")) {
                if (sp[1] != null) {
                    if (sp[1].equals("ms:")) {
                        if (sp[2] != null) {
                            Thread.sleep(Integer.parseInt(sp[2]));
                        }
                    } else {
                        Logger.logDebug("Error: windowTitle requires an argument 'title'.");
                    }
                }
            } else if (sp[0].equals("#")) {
                // do nothing
            }
        }
    }
}
