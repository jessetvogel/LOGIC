package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Labels {

    private static final Pattern patternLabel = Pattern.compile("^[A-Za-z0-9]+$");

    public static boolean valid(String label) {
        return patternLabel.matcher(label).find();
    }

    public static void apply(LabelSet<Sense> labelSet, ArrayList<Token> tokens) {
        // Replace every StringToken with a sense if the string is a label
        int size = tokens.size();
        for(int i = 0; i < size; i ++) {
            Token token = tokens.get(i);

            if(token instanceof Token.StringToken) {
                String str = ((Token.StringToken) token).str;
                if(labelSet.isSet(str)) {
                    Sense sense = labelSet.get(str);
                    tokens.remove(i);
                    tokens.add(i, new Token.SenseToken(sense));
                }
            }
        }
    }
}
