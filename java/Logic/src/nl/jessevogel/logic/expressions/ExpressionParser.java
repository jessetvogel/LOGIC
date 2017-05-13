package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpressionParser {

    private static final Map<Sense, Sense> placeholders = new HashMap<>();

    private ArrayList<Token> tokens;

    public ExpressionParser(ArrayList<Token> tokens) {
        // Store the list of tokens
        this.tokens = tokens;

        // Check if the list is empty
        if(tokens.size() == 0)
            Log.warning("Created an ExpressionParser, but empty list was passed");
    }

    public Sense parse() {
        // The goal is to reduce the whole list to one single token (Token.SenseToken)
        boolean keepParsing = true;
        while(keepParsing && (tokens.size() > 1 || !(tokens.get(0) instanceof Token.SenseToken))) {
            keepParsing = false;
            for (Rule rule : Rule.rules) {
                if(apply(rule)) {
                    keepParsing = true;
                    break;
                }
            }
        }

        // If not succeeded, return null
        if(tokens.size() > 1) return null;
        Token token = tokens.get(0);
        if(!(token instanceof Token.SenseToken)) return null;
        return ((Token.SenseToken) token).sense;
    }

    private boolean apply(Rule rule) {
        // First check if the expression associated to the rule can be found in the list of tokens
        Expression expression = rule.getExpression();
        int startingIndex = expression.find(tokens, placeholders);
        if(startingIndex < 0) return false;

        // If so, actually apply the rule:

        // Remove the expression itself from the list
        int length = expression.length();
        for(int i = 0; i < length; i ++)
            tokens.remove(startingIndex);

        // Insert the sense associated to this rule
        tokens.add(startingIndex, new Token.SenseToken(Placeholder.replace(rule.getSense(), placeholders)));
        return true;
    }
}
