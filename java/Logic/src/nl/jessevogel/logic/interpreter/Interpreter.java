package nl.jessevogel.logic.interpreter;

import java.io.File;

public class Interpreter {

    private final String workingDirectory;
    private final Parser parser;

    public Interpreter(String filename) {
        // Setup for interpreting
        parser = new Parser(filename);
        workingDirectory = (new File(filename)).getParent();
    }

    public void interpret() {
        // Parse with the parser!
        parser.parse();

        // Execute all commands
        for(Command command : parser.getCommands()) {
            switch(command.getType()) {
                case INCLUDE:
                    FileManager.loadFile(workingDirectory + "/" +  parser.getLexer().createString(command.getStartPosition(0), command.getEndPosition(0)));
                    break;
            }
        }
    }

}
