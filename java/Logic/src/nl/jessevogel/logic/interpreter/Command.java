package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

public class Command {

    private Type type;
    private Lexer lexer;
    private int amountOfArguments;
    private int[] startPositions, endPositions;

    public enum Type {
        INCLUDE
    }

    public Command setLexer(Lexer lexer) {
        // Store lexer
        this.lexer = lexer;
        return this;
    }

    public Command setType(Type type) {
        // If type was already set, give a warning
        if(this.type != null) {
            Log.warning("Setting type of command to " + type + " while it was already set to " + this.type);
        }

        // Store type and set amount of arguments
        this.type = type;
        amountOfArguments = -1;
        switch(type) {
            case INCLUDE:
                amountOfArguments = 1;
                break;
        }

        // If amountOfArguments was not set, give an error
        if(amountOfArguments < 0) {
            Log.error("Amount of arguments not defined for Command of type " + type);
            return this;
        }

        // Create arrays of positions and set the initial values to -1
        if(amountOfArguments > 0) {
            startPositions = new int[amountOfArguments];
            endPositions = new int[amountOfArguments];
            for(int i = 0;i < amountOfArguments;i ++)
                startPositions[i] = endPositions[i] = -1;
        }

        return this;
    }

    public Command setArgument(int argument, int startPosition, int endPosition) {
        // First check if type is already determined. If not, give a warning.
        if(type == null) {
            Log.warning("Trying to add argument to Command, but its type was not yet set");
            return this;
        }

        // Check if argument falls into the correct range
        if(argument < 0 || argument >= amountOfArguments) {
            Log.warning("Command of type " + type + " requires " + amountOfArguments + " arguments, but tried to set argument " + argument);
            return this;
        }

        // Check if lexer was already set
        if(lexer == null) {
            Log.warning("Trying to add argument to Command, but Lexer was not yet specified");
            return this;
        }

        // Check if startPosition and endPosition make sense
        if(startPosition > endPosition) {
            Log.warning("Start position of argument given comes later then the end position");
            return this;
        }

        // TODO: check if startPosition and endPosition make sense with respect to lexer and its range.

        // Set argument positions
        startPositions[argument] = startPosition;
        endPositions[argument] = endPosition;

        return this;
    }

    public int getAmountOfArguments() {
        // Return the amount of arguments
        return amountOfArguments;
    }

    public Type getType() {
        // Return the type
        return type;
    }

    public int getStartPosition(int argument) {
        // Check if argument falls in correct range
        if(argument < 0 || argument >= amountOfArguments) {
            Log.error("Requested the start position of argument " + (argument + 1) + ", but there are only " + amountOfArguments);
            return -1;
        }

        // Return the starting position of the argument
        return startPositions[argument];
    }

    public int getEndPosition(int argument) {
        // Check if argument falls in correct range
        if(argument < 0 || argument >= amountOfArguments) {
            Log.error("Requested the end position of argument " + (argument + 1) + ", but there are only " + amountOfArguments);
            return -1;
        }

        // Return the starting position of the argument
        return endPositions[argument];
    }
}
