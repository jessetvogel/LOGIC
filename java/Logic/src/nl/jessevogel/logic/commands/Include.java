package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.interpreter.FileManager;

public class Include extends Command {

    /*
        Syntax:
            Include[filename]
     */

    public static final String COMMAND_NAME = "Include";

    public Include() {
        // Set values belonging to this command
        amountOfArguments = 1;
        commandName = COMMAND_NAME;
        createPositionArrays();
    }

    public boolean execute() {
        // Load file
        String filename = lexer.getInterpreter().getWorkingDirectory() + "/" + lexer.createString(startPositions[0], endPositions[0]);
        if(!FileManager.loadFile(filename)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0] - 2), "Unable to load file " + filename); // TODO: use better method to find correct token
        }
        return true;
    }
}
