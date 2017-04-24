package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.interpreter.Lexer;
import nl.jessevogel.logic.log.Log;

public abstract class Command {

    protected String commandName;
    protected int amountOfArguments;
    protected Lexer lexer;
    protected int[] startPositions, endPositions;

    public void createPositionArrays() {
        // Create arrays of positions and set the initial values to -1
        if(amountOfArguments > 0) {
            startPositions = new int[amountOfArguments];
            endPositions = new int[amountOfArguments];
            for(int i = 0;i < amountOfArguments;i ++)
                startPositions[i] = endPositions[i] = -1;
        }
    }

    public Command setLexer(Lexer lexer) {
        // Store lexer
        this.lexer = lexer;
        return this;
    }

    public Command setArgument(int argument, int startPosition, int endPosition) {
        // Check if argument falls into the correct range. If not, stop. The parser will give an error
        if(argument < 0 || argument >= amountOfArguments) return this;

        // Check if lexer was already set
        if(lexer == null) {
            Log.error("Trying to add argument to Command, but Lexer was not yet specified");
            return this;
        }

        // Check if startPosition and endPosition make sense
        if(startPosition > endPosition) {
            Log.error("Start position of argument given comes later then the end position");
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

    public String getCommandName() {
        // Return the command name
        return commandName;
    }

    public abstract boolean execute();

    public static Command create(String commandName) {
        // A list of allowed commands
        if(commandName.equals("Include")) return new Include();
        if(commandName.equals("DefineType")) return new DefineType();
        if(commandName.equals("SetParentType")) return new SetParentType();
        if(commandName.equals("DefineRelation")) return new DefineRelation();
        if(commandName.equals("Let")) return new Let();

        // If not in the list, return null
        return null;
    }
}
