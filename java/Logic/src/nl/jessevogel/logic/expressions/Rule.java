package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.basic.Relation;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;

public class Rule {

    static final ArrayList<Rule> rules = new ArrayList<>();

    private Expression expression;
    private Sense sense;

    private Rule(Expression expression, Sense sense) {
        // Store
        this.expression = expression;
        this.sense = sense;
    }

    public static void addRule(Expression expression, Sense sense) {
        // Add a rule to the beginning of the list of rules, to make sure it gets applied before any earlier defined rule
        rules.add(0, new Rule(expression, sense));
    }

    Expression getExpression() {
        return expression;
    }

    public Sense getSense() {
        return sense;
    }

    public static void addRuleFromRelation(Relation relation) {
        // Requires that the label of the Relation is set
        if(relation.getLabel() == null) {
            Log.warning("Cannot create rule for Relation whose label is not set");
            return;
        }

        // Create a rule for this expression of the form: label(dependency 1, dependency 2, ... , dependency n)
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token.StringToken(relation.getLabel()));

        if(relation.placeholders != null) {
            int n = relation.amountOfDependencies;
            tokens.add(new Token.CharToken(Constant.CHARACTER_START_ARGUMENTS));

            for(int i = 0; i < n; i++) {
                tokens.add(new Token.SenseToken(relation.placeholders[i]));
                if (i < n - 1) tokens.add(new Token.CharToken(Constant.CHARACTER_SEPARATOR_ARGUMENTS));
            }

            tokens.add(new Token.CharToken(Constant.CHARACTER_END_ARGUMENTS));
        }

        // Add rule
        Expression expression = new Expression(tokens);
        Sense sense = Sense.create(relation, relation.placeholders);
        Rule.addRule(expression, sense);
    }
}
