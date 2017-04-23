package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;

public class Parser {

    public static final char CHARACTER_START_ARGUMENT = '[';
    public static final char CHARACTER_END_ARGUMENT = ']';

    private final Lexer lexer;
    private ArrayList<Command> commands;
    private Token currentToken;

    private int amountOfCommands;

    public Parser(String filename) {
        // Setup for parsing
        lexer = new Lexer(filename);
    }

    public void parse() {
        // Analyze with the lexer!
        lexer.analyze();

        // Create a new list of commands
        commands = new ArrayList<>();
        currentToken = lexer.firstToken();
        Command command;
        while((command = readCommand()) != null) {
            // Add it to the list
            commands.add(command);
        }

        // Count how many commands there are
        amountOfCommands = commands.size();
    }

    public Command readCommand() {
        // If we reached the end of the file, stop and return null
        if(lexer.reachedEnd()) return null;

        // Check for a new command
        if(currentToken instanceof Token.StringToken) {
            Token.StringToken strToken = (Token.StringToken) currentToken;
            Command command = null;

            // Define a number of commands
            if(strToken.str.equals("Include"))
                command = (new Command()).setType(Command.Type.INCLUDE);

            // If it did not match any of the above, give an error
            if(command == null) {
                Log.warning("Command '" + strToken.str + "' was not defined");
                return null;
            }

            // Set the lexer of the command
            command.setLexer(lexer);

            // Read the arguments
            currentToken = lexer.nextToken();
            int argument = 0;
            while(readArgument(command, argument))
                argument ++;

            // If the number of arguments does not match, give a warning
            if(argument != command.getAmountOfArguments()) {
                Log.warning("Command of type " + command.getType() + " expects " + command.getAmountOfArguments() + " arguments, but " + argument + " were given");
                return null;
            }

            return command;
        }

        // Could not find command
        Log.warning("Unexpected token, expected a Command (TODO: at what line and at what position)");
        return null;
    }

    private boolean readArgument(Command command, int argument) {
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
            Log.warning("File ended before end of the argument");
            return false;
        }

        int endPosition = lexer.getPosition();
        currentToken = lexer.nextToken();

        // Set the argument of the command, and return true
        command.setArgument(argument, startPosition, endPosition);
        return true;
    }

    public ArrayList<Command> getCommands() {
        // Return the list of commands
        return commands;
    }

    public Lexer getLexer() {
        // Return the lexer
        return lexer;
    }
}
