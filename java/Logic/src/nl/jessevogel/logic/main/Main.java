package nl.jessevogel.logic.main;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.interpreter.FileManager;
import nl.jessevogel.logic.log.Log;

public class Main {

    public static void main(String[] args) {

        Log.output("Initializing...");

        // Initialize all constants
        Constant.initialize();

        Log.output("Parsing root file");

        // Read main.math
        FileManager.loadRootFile();

        Log.output("Done");
    }
}
