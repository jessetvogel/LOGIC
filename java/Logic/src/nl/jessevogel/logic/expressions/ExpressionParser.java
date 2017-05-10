package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;

public class ExpressionParser {

    private ArrayList<Token> tokens;

    public ExpressionParser(ArrayList<Token> tokens) {
        // Store the list of tokens
        this.tokens = tokens;
    }

    public Sense parse() {

        return null;
    }
}
