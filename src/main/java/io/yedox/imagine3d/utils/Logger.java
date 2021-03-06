package io.yedox.imagine3d.utils;

public class Logger {
    private static String logType = "DEBUG";
    private static String logSource = "Client";

    public static void logDebug(Object in) {
        System.out.println("[" + logSource + "/" + logType + "] " + in);
    }

    public static void logDebug(Object in, String color) {
        System.out.println(color + "[" + logSource + "/" + logType + "] " + in + ANSIConstants.ANSI_RESET);
    }

    public static void logModDebug(Object in) {
        setLogType("DEBUG");
        setLogSource("Mod");
        System.out.println("[" + logSource + "/" + logType + "] " + in);
        setLogSource("Client");
    }

    public static void logModError(Object in) {
        setLogType("ERROR");
        setLogSource("Mod");
        System.err.println("[" + logSource + "/" + logType + "] " + in);
        setLogType("DEBUG");
        setLogSource("Client");
    }


    public static void logError(Object in) {
        setLogSource("Client");
        setLogType("ERROR");
        System.err.println("[" + logSource + "/" + logType + "] " + in);
        setLogType("DEBUG");
        setLogSource("Client");
    }

    public static void logLuaError(Object in) {
        setLogSource("LuaEngine");
        setLogType("ERROR");
        System.err.println("[" + logSource + "/" + logType + "] " + in);
        setLogType("DEBUG");
        setLogSource("Client");
    }

    public static void logLuaDebug(Object in) {
        setLogSource("LuaEngine");
        setLogType("DEBUG");
        System.out.println("[" + logSource + "/" + logType + "] " + in);
        setLogSource("Client");
    }

    public static void logLuaScriptError(Object in) {
        setLogSource("LuaEngine:Script");
        setLogType("ERROR");
        System.err.println("[" + logSource + "/" + logType + "] " + in);
        setLogType("DEBUG");
        setLogSource("Client");
    }

    public static void logLuaScriptDebug(Object in) {
        setLogSource("LuaEngine:Script");
        setLogType("DEBUG");
        System.out.println("[" + logSource + "/" + logType + "] " + in);
        setLogSource("Client");
    }


    public static void setLogType(String logtype) {
        logType = logtype;
    }

    public static void setLogSource(String logsource) {
        logSource = logsource;
    }
}
