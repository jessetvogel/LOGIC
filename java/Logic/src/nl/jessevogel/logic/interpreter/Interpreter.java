package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.io.File;

public class Interpreter {

    private final String filename;
    private final String workingDirectory;
    private final Parser parser;

    Interpreter(String filename) {
        // Setup for interpreting
        this.filename = (new File(filename)).getAbsolutePath();
        parser = new Parser(filename);
        parser.setInterpreter(this);
        workingDirectory = (new File(filename)).getParent();
    }

    boolean interpret() {
        // Parse with the parser!
        return parser.parse();
    }

    public String getWorkingDirectory() {
        // Return the working directory
        return workingDirectory;
    }

    public void error(Token token, String message) {
        Log.output("Error in " + filename + " at line " + token.getLine() + " at position " + (token.getColumn() + 1) + ":");
        Log.output("\t " + message);
        Log.output("");
    }

    public void error(int line, int column, String message) {
        Log.output("Error in " + filename + " at line " + line + " at position " + (column + 1) + ":");
        Log.output("\t " + message);
        Log.output("");
    }
}
