package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public final static Pattern patternType = Pattern.compile("^<([A-Za-z]+)>$");

    public final String strExpression;
    public Expression expression;
    public int pointer;
    public int length;

    public Parser(String expression) {
        // Save expression
        strExpression = expression;

        // Setup for parsing
        pointer = 0;
        length = expression.length();
    }

    public Expression parse() {
        // If was already parsed, return the result
        if(expression != null) return expression;

        // Create a new expresion
        expression = new Expression();
        String[] parts = strExpression.split("\\s");
        for(int i = 0;i < parts.length;i ++) {
            add(expression, parts[i]);
        }

        return expression;
    }

    public void add(Expression expression, String part) {
        // Check for something of the form "<Type>"
        Matcher m = patternType.matcher(part);
        if(m.find()) {
            Type type = Type.labels.get(m.group(1));
            if(type != null) {
                expression.addTerm(new Expression.TypeTerm(null));
                return;
            }
        }

        // TODO: more possibilities

        // By default, add as string
        expression.addTerm(new Expression.StringTerm(part));
    }

}
