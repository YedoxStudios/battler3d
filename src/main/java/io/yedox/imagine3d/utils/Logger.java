package io.yedox.imagine3d.utils;

public class Logger {
    private static String logType = "DEBUG";
    private static String logSource = "Client";

    public static void logDebug(Object in) {
        System.out.println("[" + logSource + "/" + logType + "] " + in);
    }

    public static void logModDebug(Object in) {
        setLogType("DEBUG");
        setLogSource("Mod");
        System.out.println("[" + logSource + "/" + logType + "] " + in);
        setLogType("DEBUG");
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

    public static void setLogType(String logtype) {
        logType = logtype;
    }

    public static void setLogSource(String logsource) {
        logSource = logsource;
    }
}
