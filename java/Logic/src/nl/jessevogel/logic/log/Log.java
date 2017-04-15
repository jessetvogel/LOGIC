package nl.jessevogel.logic.log;

public class Log {

    public static void error(String message) {
        System.err.println("[ERROR] " + message);
    }

    public static void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }

    public static void warning(String message) {
        System.out.println("[WARNING] " + message);
    }

}
