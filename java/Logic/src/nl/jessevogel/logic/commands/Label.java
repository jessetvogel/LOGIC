package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.*;
import nl.jessevogel.logic.expressions.ExpressionParser;
import nl.jessevogel.logic.expressions.Labels;
import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;

public class Label extends Command {

    /*
        Syntax:
            Label[label][sense]
     */

    private static final String COMMAND_NAME = "Label";

    private int argumentCounter;
    private String label;
    private Sense sense;

    private boolean error;

    Label() {
        // Set values belonging to this command
        commandName = COMMAND_NAME;

        // Set other values
        argumentCounter = 0;
        error = false;
    }

    void interpretArgument(int startPosition, int endPosition) {
        if(error) return;

        switch(argumentCounter) {
            case 0:
                setLabel(startPosition, endPosition);
                break;

            case 1:
                setSense(startPosition, endPosition);
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
        if(error)
            return false;

        if(label == null || sense == null) {
            lexer.getInterpreter().error(firstToken, "Missing arguments");
            return false;
        }

        // Map label to this sense
        Scope.main.labelSenses.put(label, sense);
        return true;
    }

    private void setLabel(int startPosition, int endPosition) {
        // Check if the label has the correct type name pattern
        String label = lexer.createString(startPosition, endPosition);
        if(!Labels.valid(label)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition),"Relation label may only contain alphanumerical characters");
            error = true;
            return;
        }

        if(Scope.main.labelSenses.isSet(label)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition),"The label '" + label + "' was already used");
            error = true;
            return;
        }

        // Store the label
        this.label = label;
    }

    private void setSense(int startPosition, int endPosition) {
        // First apply the known labels
        ArrayList<Token> senseTokens = lexer.createArray(startPosition, endPosition);
        Labels.apply(Scope.main.labelSenses, senseTokens);

        // Parse the given expression
        Sense sense = (new ExpressionParser(senseTokens)).parse();
        if(sense == null) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Was not able to parse the provided argument");
            error = true;
            return;
        }

        // Store the sense
        this.sense = sense;
    }
}
