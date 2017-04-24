package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.commands.Command;
import nl.jessevogel.logic.log.Log;

import java.io.File;

public class Interpreter {

    private final String filename;
    private final String workingDirectory;
    private final Parser parser;

    public Interpreter(String filename) {
        // Setup for interpreting
        this.filename = filename;
        parser = new Parser(filename);
        parser.setInterpreter(this);
        workingDirectory = (new File(filename)).getParent();
    }

    public void interpret() {
        // Parse with the parser!
        parser.parse();

        // Execute all commands
        for(Command command : parser.getCommands())
            command.execute();
    }

    public String getWorkingDirectory() {
        // Return the working directory
        return workingDirectory;
    }

    public void error(Token token, String message) {
        Log.output("Error in " + filename + " at line " + token.getLine() + " at position " + token.getColumn() + ":");
        Log.output("\t " + message);
        Log.output("");
    }

    public void error(int line, int column, String message) {
        Log.output("Error in " + filename + " at line " + line + " at position " + column + ":");
        Log.output("\t " + message);
        Log.output("");
    }
}
