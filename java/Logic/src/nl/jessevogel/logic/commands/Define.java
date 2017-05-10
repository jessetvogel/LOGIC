package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.basic.Relation;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.expressions.ExpressionParser;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Define extends Command {

    /*
        Syntax:
            Define[label][type][type 1][type 2] ... [type n]
     */

    private static final String COMMAND_NAME = "Define";
    private static final Pattern patternRelationName = Pattern.compile("^[A-Za-z0-9]+$");

    private int argumentCounter;
    private String label;
    private Sense type;
    private ArrayList<Sense> dependencies;

    private boolean error;

    Define() {
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
                addDependency(startPosition, endPosition);
                break;
        }

        // Increase the argument counter by one
        argumentCounter ++;
    }

    public boolean execute() {
        if(error)
            return false; // TODO: maybe give an error message?

        // Create a new array from the ArrayList
        Sense[] senses = dependencies.toArray(new Sense[dependencies.size()]);

        // Create the relation and give it a label
        (new Relation(type, senses))
                .setLabel(label);

        return true;
    }

    private void setLabel(int startPosition, int endPosition) {
        // Check if the label has the correct type name pattern
        String label = lexer.createString(startPosition, endPosition);
        if(!patternRelationName.matcher(label).find()) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition),"Relation label may only contain alphanumerical characters");
            error = true;
            return;
        }

        // Construct label for Relation
        if(Relation.exists(label)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition),"Relation '" + label + "' was already defined");
            error = true;
            return;
        }

        // Store the label
        this.label = label;
    }

    private void setType(int startPosition, int endPosition) {
        // Find the type of the relation, and check if it exists
        Sense type = (new ExpressionParser(lexer.createArray(startPosition, endPosition))).parse();
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

    private void addDependency(int startPosition, int endPosition) {
        // Parse the sense
        Sense type = (new ExpressionParser(lexer.createArray(startPosition, endPosition))).parse();
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

        // Add the type to the list of dependencies
        if(dependencies == null)
            dependencies = new ArrayList<>();
        dependencies.add(type);
    }
}
