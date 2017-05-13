package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.interpreter.FileManager;

public class Include extends Command {

    /*
        Syntax:
            Include[filename]
     */

    private static final String COMMAND_NAME = "Include";

    private int argumentCounter;
    private String filename;
    private int firstTokenPosition;

    private boolean error;

    Include() {
        // Set values belonging to this command
        commandName = COMMAND_NAME;

        // Set other values
        argumentCounter = 0;
        error = false;
    }

    void interpretArgument(int startPosition, int endPosition) {
        if(error) return;

        switch(argumentCounter) {
            case 0:
                setFilename(startPosition, endPosition);
                break;

            default:
                lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Only one argument expected");
                error = true;
                break;
        }

        // Increase the argument counter by one
        argumentCounter ++;
    }

    private void setFilename(int startPosition, int endPosition) {
        // Determine filename and set firstTokenPosition
        filename = lexer.getInterpreter().getWorkingDirectory() + "/" + lexer.createString(startPosition, endPosition);
        firstTokenPosition = startPosition;
    }

    public boolean execute() {
        if(error || filename == null)
            return false; // TODO: ?

        // Load file
        if(!FileManager.loadFile(filename)) {
            lexer.getInterpreter().error(lexer.tokenAt(firstTokenPosition), "Unable to load file " + filename);
        }
        return true;
    }
}
