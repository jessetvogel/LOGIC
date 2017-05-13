package nl.jessevogel.logic.main;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.interpreter.FileManager;
import nl.jessevogel.logic.log.Log;

public class Main {

    public static void main(String[] args) {
        // Initialize all constants
        Log.output("Initializing");
        Constant.initialize();

        // Read main.math
        Log.output("Parsing root file");
        FileManager.loadRootFile();

        // Done
        Log.output("Done");
    }
}
