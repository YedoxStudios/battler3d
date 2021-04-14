package io.yedox.imagine3d.util;

public class Logger {
    private static String logType = "Debug";
    private static String logSource = "Client";

    public static void logDebug(Object in) {
        System.out.println("[" + logSource + "/" + logType + "] " + in);
    }

    public static void logMod(Object in) {
        setLogSource("Mod");
        System.out.println("[" + logSource + "/" + logType + "] " + in);
        setLogType("Debug");
    }

    public static void logError(Object in) {
        setLogType("Error");
        System.err.println("[" + logSource + "/" + logType + "] " + in);
        setLogType("Debug");
    }

    public static void setLogType(String logtype) {
        logType = logtype;
    }

    public static void setLogSource(String logsource) {
        logSource = logsource;
    }
}