package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.Map;

public class Expression {

    private final Token[] tokens;

    public Expression(Token[] tokens) {
        // Copy the array of tokens
        this.tokens = tokens;
    }

    public Expression(ArrayList<Token> tokens) {
        // Create an array out of the passed ArrayList
        this.tokens = tokens.toArray(new Token[tokens.size()]);
    }

    int length() {
        return tokens.length;
    }

    public int find(ArrayList<Token> listToken, Map<Sense, Sense> placeholders) {
        placeholders.clear(); // TODO: when should we clear this even? I guess it doesn't even need to be cleared, but maybe just for performance?
        int size = listToken.size();
        int maxStartingIndex = size - tokens.length;

        for(int startingIndex = 0; startingIndex <= maxStartingIndex; startingIndex ++) { // TODO: format all for-loops equally
            int i;
            for(i = 0;i < tokens.length;i ++) {
                Token token1 = tokens[i];
                Token token2 = listToken.get(startingIndex + i);

                // Check if they match as characters
                if(token1 instanceof Token.CharToken && token2 instanceof Token.CharToken) {
                    if(((Token.CharToken) token1).c == ((Token.CharToken) token2).c) continue; else break;
                }

                // Check if they match as strings
                if(token1 instanceof Token.StringToken && token2 instanceof Token.StringToken) {
                    if(((Token.StringToken) token1).str.equals(((Token.StringToken) token2).str)) continue; else break;
                }

                // Check if they match as senses
                if(token1 instanceof Token.SenseToken && token2 instanceof Token.SenseToken) {
                    Sense sense1 = ((Token.SenseToken) token1).sense;
                    Sense sense2 = ((Token.SenseToken) token2).sense;

                                        // TODO: this may be unnessasary, but yeah..
                                        if(!Placeholder.is(sense1)) {
                                            Log.error("Expression turns out to contain some SenseToken that is not a placeholder..");
                                            return -1;
                                        }

                    // TODO: account for children of the type
                    if(sense2.relation.getType() == sense1.relation.getType()) {
                        placeholders.put(sense1, sense2);
                        continue;
                    } else break;
                }

                // If none of the above triggered, then there is no match
                break;
            }

            // If there is a match,
            if(i == tokens.length)
                return startingIndex;
        }

        return -1;
    }





    // TODO: remove this

    public void print() {
        for(Token token : tokens) {
            if(token instanceof Token.CharToken)
                System.out.print(((Token.CharToken) token).c);

            if(token instanceof Token.StringToken)
                System.out.print(((Token.StringToken) token).str);

            if(token instanceof Token.SenseToken)
                System.out.print(((Token.SenseToken) token).sense.relation.getLabel());
        }

        System.out.println();
    }

}

