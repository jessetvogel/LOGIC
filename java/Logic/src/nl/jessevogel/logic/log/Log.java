package nl.jessevogel.logic.log;

public class Log {

    public static void error(String message) {
        System.err.println(" > [ERROR] " + message);
    }

    public static void warning(String message) {
        System.out.println(" > [WARNING] " + message);
    }

    public static void output(String message) {
        System.out.println(" > " + message);
    }
}
