package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.interpreter.Lexer;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

public abstract class Command {

    String commandName;
    Lexer lexer;
    protected Token firstToken;

    public Command setLexer(Lexer lexer) {
        // Store lexer
        this.lexer = lexer;
        return this;
    }

    public Command setFirstToken(Token firstToken) {
        // Store firstToken
        this.firstToken = firstToken;
        return this;
    }

    public Command addArgument(int startPosition, int endPosition) {
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

        interpretArgument(startPosition, endPosition);
        return this;
    }

    abstract void interpretArgument(int startPosition, int endPosition);

    public abstract boolean execute();

    public static Command create(String commandName) {
        // A list of allowed commands
        if(commandName.equals("Include")) return new Include();
        if(commandName.equals("Define")) return new Define();
        if(commandName.equals("Notation")) return new Notation();
        if(commandName.equals("Let")) return new Let();
        if(commandName.equals("Call")) return new Call();

        if(commandName.equals("Print")) return new Print();

        // If not in the list, return null
        return null;
    }
}
