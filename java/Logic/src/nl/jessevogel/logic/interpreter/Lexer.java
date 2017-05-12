package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;

public class Lexer {

    private Interpreter interpreter;
    private Scanner scanner;
    private ArrayList<Token> tokens;
    private char currentChar;

    private static final Token TOKEN_IGNORE = new Token();
    private static final String specialCharacters = "(){}[]<>.,_-+*/\\^!?";
    private static final String wordCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int amountOfTokens;
    private int position;
    private Token currentToken;

    public Lexer(String filename) {
        // Setup for analyzing
        scanner = new Scanner(filename);
    }

    public void setInterpreter(Interpreter interpreter) {
        // Check if interpreter was already set, if so, give a warning
        if(this.interpreter != null)
            Log.warning("Trying to set interpreter, while it was already set");

        this.interpreter = interpreter;
    }

    public boolean analyze() {
        if(tokens != null) {
            // If already scanned, give a warning and stop
            Log.warning("Lexer.analyze() called while already analyzed");
            return false;
        }

        // Scan with the scanner!
        if(!scanner.scan()) return false;

        // Group characters to tokens
        tokens = new ArrayList<>();
        currentChar = scanner.firstCharacter();
        Token token;
        while((token = readToken()) != null) {
            // If this token should be ignored, continue
            if(token == TOKEN_IGNORE) continue;

            // Otherwise, add it to the list of tokens
            tokens.add(token);
        }
        if(!scanner.reachedEnd()) return false;

        // Count how many tokens there are
        amountOfTokens = tokens.size();
        return true;
    }

    private Token readToken() {
        // If we reached the end of the file, stop and return null
        if(scanner.reachedEnd()) return null;

        // Check for whitespace, if any was found, ignore it
        if (java.lang.Character.isWhitespace(currentChar)) {
            currentChar = scanner.nextCharacter();
            while (java.lang.Character.isWhitespace(currentChar)) {
                currentChar = scanner.nextCharacter();
            }
            return TOKEN_IGNORE;
        }

        // Check for comments, if any was found, ignore it
        if (currentChar == '#' && scanner.getColumn() == 0) {
            int currentLine = scanner.getLine();
            currentChar = scanner.nextCharacter();
            while(!scanner.reachedEnd() && scanner.getLine() == currentLine)
                currentChar = scanner.nextCharacter();

            return TOKEN_IGNORE;
        }

        // Check for special characters
        if (specialCharacters.indexOf(currentChar) != -1) {
            Token token = (new Token.CharToken(currentChar))
                    .setLine(scanner.getLine())
                    .setColumn(scanner.getColumn() - 1);
            currentChar = scanner.nextCharacter();
            return token;
        }

        // Check for words
        if (wordCharacters.indexOf(currentChar) != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(currentChar);
            currentChar = scanner.nextCharacter();
            while (wordCharacters.indexOf(currentChar) != -1) {
                sb.append(currentChar);
                currentChar = scanner.nextCharacter();
            }
            return (new Token.StringToken(sb.toString()))
                    .setLine(scanner.getLine())
                    .setColumn(scanner.getColumn() - sb.length());
        }

        // If none of the above was the case, give a warning
        interpreter.error(scanner.getLine(), scanner.getColumn(),"Unexpected symbol '" + currentChar + "'");
        return null;
    }

    public Token firstToken() {
        if(tokens == null) {
            // If not yet analyzed, give a warning, and then analyze
            Log.warning("Scanner.firstToken() was called, but there was not yet analyzed");
            analyze();
        }

        // Set position to beginning, and return the first character
        position = 0;
        currentToken = tokens.get(0);
        return currentToken;
    }

    public Token nextToken() {
        // If we already reached the end of the list of tokens, return null
        if(reachedEnd()) return null;

        // Update position
        position ++;
        if(reachedEnd()) return null;

        // Get new character and return it
        currentToken = tokens.get(position);
        return currentToken;
    }

    public int getPosition() {
        // Return the current position
        return position;
    }

    public Interpreter getInterpreter() {
        // Return the interpreter
        return interpreter;
    }

    public boolean reachedEnd() {
        return position == amountOfTokens;
    }

    public String createString(int startPosition, int endPosition) {
        // Create a string by concatenating the contents of the tokens between start and end position
        StringBuilder sb = new StringBuilder();
        for (int i = startPosition; i < endPosition; i++) {
            Token token = tokens.get(i);
            if (token instanceof Token.CharToken)
                sb.append(((Token.CharToken) token).c);

            if (token instanceof Token.StringToken)
                sb.append(((Token.StringToken) token).str);
        }
        return sb.toString();
    }

    public ArrayList<Token> createArray(int startPosition, int endPosition) {
        // Create an array of tokens between start and end position
        ArrayList<Token> array = new ArrayList<>();
        for (int i = startPosition; i < endPosition; i++)
            array.add(tokens.get(i));

        return array;
    }

    public Token tokenAt(int position) {
        // Return the token at the given position
        return tokens.get(position);
    }
}
