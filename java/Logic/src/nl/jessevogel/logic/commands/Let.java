package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.*;
import nl.jessevogel.logic.expressions.ExpressionParser;
import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Let extends Command {

    /*
        Syntax:
            Let[label][type]
     */

    private static final String COMMAND_NAME = "Let";
    private static final Pattern patternLabel = Pattern.compile("^[A-Za-z0-9]+$");

    private int argumentCounter;
    private String label;
    private Sense type;

    private boolean error;

    Let() {
        // Set values belonging to this command
        commandName = COMMAND_NAME;

        // Set other values
        argumentCounter = 0;
        error = false;
    }

    void interpretArgument(int startPosition, int endPosition) {
        switch(argumentCounter) {
            case 0:
                setLabel(startPosition, endPosition);
                break;

            case 1:
                setType(startPosition, endPosition);
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
        if(error || label == null || type == null)
            return false; // TODO: ?

        // Create a sense
        Sense sense = Instance.create(type);

        // Map the sense to a new referent
        Scope.main.map(sense, Referent.createInstance());

        // Map label to this sense
        Scope.main.labelSenses.put(label, sense);

        return true;
    }

    private void setLabel(int startPosition, int endPosition) {
        // Check if the label has the correct type name pattern
        String label = lexer.createString(startPosition, endPosition);
        if(!patternLabel.matcher(label).find()) {
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

    private void setType(int startPosition, int endPosition) {
        // Find the type of the relation, and check if it exists
        Sense type = (new ExpressionParser(Scope.main.labelSenses.substituteTokens(lexer.createArray(startPosition, endPosition)))).parse();
        if(type == null) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Was not able to parse the provided argument");
            error = true;
            return;
        }

        // Check if the parsed Sense is actually a type
        if(type.relation.getType() != Constant.TYPE_TYPE) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Expected a type");
            error = true;
            return;
        }

        // Store the type
        this.type = type;
    }
}
