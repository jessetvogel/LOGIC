package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FileManager {

    private static final String rootFile = "../../math/main.math"; // TODO: some other path, but this is handy while working on the project

    private static Set<String> loadedFiles = new HashSet<>();

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
        if(!loadFile(rootFile))
            Log.warning("Root file " + rootFile + " was not found");
    }
}
