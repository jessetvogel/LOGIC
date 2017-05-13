package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.*;
import nl.jessevogel.logic.expressions.*;
import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;

public class Let extends Command {

    /*
        Syntax:
            Let[label][type]
     */

    private static final String COMMAND_NAME = "Let";

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
        if(error) return;

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
        if(error)
            return false;

        if(label == null || type == null) {
            lexer.getInterpreter().error(firstToken, "Missing arguments");
            return false;
        }

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

    private void setType(int startPosition, int endPosition) {
        // Find the type of the relation, and check if it exists
        ArrayList<Token> typeTokens = lexer.createArray(startPosition, endPosition);
        Labels.apply(Scope.main.labelSenses, typeTokens);
        Sense type = (new ExpressionParser(typeTokens)).parse();
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
