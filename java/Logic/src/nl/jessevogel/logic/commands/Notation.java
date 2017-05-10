package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.expressions.Expression;

public class Notation extends Command {

    /*
        Syntax:
            Notation[Expression new][Expression old]
     */

    private static final String COMMAND_NAME = "Notation";

    private int argumentCounter;
    private Expression expressionNew;
    private Expression expressionOld;

    private boolean error;

    Notation() {
        // Set values belonging to this command
        commandName = COMMAND_NAME;

        // Set other values
        argumentCounter = 0;
        error = false;
    }

    void interpretArgument(int startPosition, int endPosition) {
        switch(argumentCounter) {
            case 0:
                setExpressionNew(startPosition, endPosition);
                break;

            case 1:
                setExpressionOld(startPosition, endPosition);
                break;

            default:
                lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Only two arguments expected");
                error = true;
                break;
        }

        // Increase the argument counter by one
        argumentCounter ++;
    }

    public boolean execute() {
        if(error || expressionNew == null || expressionOld == null)
            return false; // TODO: maybe give an error message?

        // TODO

        return true;
    }

    private void setExpressionNew(int startPosition, int endPosition) {
        // Create a new expression from the tokens
        expressionNew = new Expression(lexer.createArray(startPosition, endPosition));
    }

    private void setExpressionOld(int startPosition, int endPosition) {
        // Create an expression from the tokens
        expressionOld = new Expression(lexer.createArray(startPosition, endPosition));
    }
}
