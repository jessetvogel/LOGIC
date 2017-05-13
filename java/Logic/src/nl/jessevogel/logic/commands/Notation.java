package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Scope;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.expressions.*;
import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;

public class Notation extends Command {

    /*
        Syntax:
            Notation[expression][sense][type 1][label 1] ... [type n][label n]

        Example:
            Notation[x in A][In(x, A)][Object][x][Set][A]
     */

    private static final String COMMAND_NAME = "Notation";

    private int argumentCounter;
    private ArrayList<Token> arrayExpression;
    private ArrayList<Token> arraySense;
    private Sense type;
    private LabelSet<Sense> labelSet;

    private boolean error;

    Notation() {
        // Set values belonging to this command
        commandName = COMMAND_NAME;

        // Set other values
        argumentCounter = 0;
        error = false;

        // By default, use the LabelSet of the main scope. If more placeholders are used, a new LabelSet will be created
        labelSet = Scope.main.labelSenses;
    }

    void interpretArgument(int startPosition, int endPosition) {
        if(error) return;

        switch(argumentCounter) {
            case 0:
                arrayExpression = lexer.createArray(startPosition, endPosition);
                break;

            case 1:
                arraySense = lexer.createArray(startPosition, endPosition);
                break;

            default:
                if(argumentCounter % 2 == 0) {
                    // Get type
                    type = (new ExpressionParser(lexer.createArray(startPosition, endPosition))).parse();

                    // TODO: check if it even is a type
                }
                else {
                    // Get label
                    String label = lexer.createString(startPosition, endPosition);

                    // Check if the label is valid
                    if(!Label.valid(label)) {
                        lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Not a valid label");
                        error = true;
                        return;
                    }

                    // Replace the labelSet if still the default is used
                    if(labelSet == Scope.main.labelSenses)
                        labelSet = new LabelSet<Sense>().addParent(Scope.main.labelSenses);

                    // Add it to the labelSet
                    labelSet.put(label, Placeholder.create(type));
                }
                break;
        }

        // Increase the argument counter by one
        argumentCounter ++;
    }

    public boolean execute() {
        // Check for errors
        if (error)
            return false;

        if (arrayExpression == null || arraySense == null) {
            lexer.getInterpreter().error(firstToken, "Command Notation requires at least two arguments");
            return false;
        }

        if(argumentCounter % 2 != 0) {
            lexer.getInterpreter().error(firstToken, "Command Notation expected label but none passed");
            return false;
        }

        // Substitute all tokens
        labelSet.substituteTokens(arrayExpression);
        labelSet.substituteTokens(arraySense);

        // Create a new rule
        Expression expression = new Expression(arrayExpression);
        Sense sense = (new ExpressionParser(arraySense)).parse();
        Rule.addRule(expression, sense);

        return true;
    }
}
