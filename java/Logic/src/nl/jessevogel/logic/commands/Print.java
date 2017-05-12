package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.interpreter.FileManager;
import nl.jessevogel.logic.log.Log;

public class Print extends Command {

    /*
        Syntax:
            Print[message]
     */

    private static final String COMMAND_NAME = "Print";

    private int argumentCounter;
    private String message;

    private boolean error;

    Print() {
        // Set values belonging to this command
        commandName = COMMAND_NAME;

        // Set other values
        argumentCounter = 0;
        error = false;
    }

    void interpretArgument(int startPosition, int endPosition) {
        switch(argumentCounter) {
            case 0:
                setMessage(startPosition, endPosition);
                break;

            default:
                lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Only one argument expected");
                error = true;
                break;
        }

        // Increase the argument counter by one
        argumentCounter ++;
    }

    private void setMessage(int startPosition, int endPosition) {
        // Determine filename and set firstTokenPosition
        message = lexer.createString(startPosition, endPosition);
    }

    public boolean execute() {
        if(error)
            return false;

        if(message == null) {
            lexer.getInterpreter().error(firstToken, "One argument expected");
            return false;
        }

        // Print message
        Log.output(message);
        return true;
    }
}
