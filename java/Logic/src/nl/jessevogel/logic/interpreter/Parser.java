package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.commands.*;
import nl.jessevogel.logic.log.Log;

class Parser {

    private Interpreter interpreter;
    private final Lexer lexer;
    private Token currentToken;

    Parser(String filename) {
        // Setup for parsing
        lexer = new Lexer(filename);
    }

    void setInterpreter(Interpreter interpreter) {
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

        // Keep reading commands and try to execute them
        currentToken = lexer.firstToken();
        Command command;
        while((command = readCommand()) != null)
            if(!command.execute())
                return false;

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
        if(cToken.c != Constant.CHARACTER_START_COMMAND_ARGUMENT) return false;

        currentToken = lexer.nextToken();

        int startPosition = lexer.getPosition();

        // Read on until we find a ']'
        while(!lexer.reachedEnd() && (!(currentToken instanceof Token.CharToken)) || ((Token.CharToken) currentToken).c != Constant.CHARACTER_END_COMMAND_ARGUMENT) {
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
}
