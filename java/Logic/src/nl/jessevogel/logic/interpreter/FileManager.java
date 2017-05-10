package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private static String rootFile = "/Users/Jesse/Documents/logic/main.math";

    public static ArrayList<String> loadedFiles = new ArrayList<>();

    public static boolean loadFile(String filename) {
        // If the file was already loaded, just return true
        if(loadedFiles.contains(filename)) return true;

        // Check if the file exists before attempting to interpret it
        File file = new File(filename);
        if(!file.isFile()) return false;

        // Add the filename to the list of loaded files, and then interpret it
        loadedFiles.add(filename);
        (new Interpreter(filename)).interpret();
        return true;
    }

    public static void loadRootFile() {
        loadFile(rootFile);
    }
}
