package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.commands.*;
import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;

public class Parser {

    private static final char CHARACTER_START_ARGUMENT = '[';
    private static final char CHARACTER_END_ARGUMENT = ']';

    private Interpreter interpreter;
    private final Lexer lexer;
    private ArrayList<Command> commands;
    private Token currentToken;

    Parser(String filename) {
        // Setup for parsing
        lexer = new Lexer(filename);
    }

    public void setInterpreter(Interpreter interpreter) {
        // Check if interpreter was already set, if so, give a warning
        if(this.interpreter != null)
            Log.warning("Trying to set interpreter, while it was already set");

        // Set interpreter, as well as the lexer's
        this.interpreter = interpreter;
        lexer.setInterpreter(interpreter);
    }

    boolean parse() {
        // Analyze with the lexer!
        if(!lexer.analyze()) return false;

        // Create a new list of commands
        commands = new ArrayList<>();
        currentToken = lexer.firstToken();
        Command command;
        while((command = readCommand()) != null) {
            // Add it to the list
            if(!command.execute()) return false;
        }

        return lexer.reachedEnd();
    }

    private Command readCommand() {
        // If we reached the end of the file, stop and return null
        if(lexer.reachedEnd()) return null;

        // Check for a new command
        if(currentToken instanceof Token.StringToken) {
            Token.StringToken commandNameToken = (Token.StringToken) currentToken;
            Command command = Command.create(commandNameToken.str);
            if(command == null) {
                interpreter.error(commandNameToken,"Command '" + commandNameToken.str + "' is undefined");
                return null;
            }

            // Set the lexer of the command
            command
                    .setLexer(lexer)
                    .setFirstToken(commandNameToken);

            // Read the arguments
            currentToken = lexer.nextToken();
            while(readArgument(command));

            return command;
        }

        // Could not find command
        interpreter.error(currentToken, "Unexpected token, expected a command");
        return null;
    }

    private boolean readArgument(Command command) {
        // First Token should be a '[', if we don't find it, return false
        if(!(currentToken instanceof Token.CharToken)) return false;
        Token.CharToken cToken = (Token.CharToken) currentToken;
        if(cToken.c != CHARACTER_START_ARGUMENT) return false;

        currentToken = lexer.nextToken();

        int startPosition = lexer.getPosition();

        // Read on until we find a ']'
        while(!lexer.reachedEnd() && (!(currentToken instanceof Token.CharToken)) || ((Token.CharToken) currentToken).c != CHARACTER_END_ARGUMENT) {
            currentToken = lexer.nextToken();
        }

        // If we reached the end before the argument ended, give a warning
        if(lexer.reachedEnd()) {
            interpreter.error(currentToken, "File ended before end of the argument");
            return false;
        }

        int endPosition = lexer.getPosition();
        currentToken = lexer.nextToken();

        // Set the argument of the command, and return true
        command.addArgument(startPosition, endPosition);
        return true;
    }

    ArrayList<Command> getCommands() {
        // Return the list of commands
        return commands;
    }
}
