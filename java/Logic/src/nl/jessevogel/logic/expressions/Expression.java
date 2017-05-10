package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;

public class Expression {

    private final Token[] tokens;

    public Expression(ArrayList<Token> tokens) {
        // Create an array out of the passed ArrayList
        this.tokens = tokens.toArray(new Token[tokens.size()]);
    }
}
