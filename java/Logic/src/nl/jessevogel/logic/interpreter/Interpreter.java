package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.commands.Command;

import java.io.File;

public class Interpreter {

    private final String workingDirectory;
    private final Parser parser;

    public Interpreter(String filename) {
        // Setup for interpreting
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
}
