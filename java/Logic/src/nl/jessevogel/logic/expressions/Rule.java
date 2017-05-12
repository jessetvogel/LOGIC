package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rule {

    public static final ArrayList<Rule> rules = new ArrayList<>();

    private Expression expression;
    private Sense sense;

    private boolean matched;
    private ArrayList<Token> givenList;
    private int startingIndex;

    private Rule(Expression expression, Sense sense) {
        // Store
        this.expression = expression;
        this.sense = sense;

        // Set other values
        matched = false;
    }

    public static void addRule(Expression expression, Sense sense) {
        // Simply add a rule to the list of rules
        rules.add(new Rule(expression, sense));
    }

    public Expression getExpression() {
        return expression;
    }

    public Sense getSense() {
        return sense;
    }
}
