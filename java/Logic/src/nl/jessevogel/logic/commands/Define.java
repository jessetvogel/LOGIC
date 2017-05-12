package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.basic.Relation;
import nl.jessevogel.logic.basic.Scope;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.expressions.*;
import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Define extends Command {

    /*
        Syntax:
            Define[label][type][type 1][label 1][type 2][label 2] ... [type n][label n]
     */

    private static final String COMMAND_NAME = "Define";
    private static final Pattern patternRelationName = Pattern.compile("^[A-Za-z0-9]+$");
    private static final char CHARACTER_START_ARGUMENTS = '(';
    private static final char CHARACTER_END_ARGUMENTS = ')';
    private static final char CHARACTER_ARGUMENTS_SEPARATOR = ',';

    private int argumentCounter;
    private String label;
    private Sense type;
    private ArrayList<Sense> dependencies;
    private LabelSet<Sense> labelSet;
    private Sense dependency;

    private boolean error;

    Define() {
        // Set values belonging to this command
        commandName = COMMAND_NAME;

        // Set other values
        argumentCounter = 0;
        error = false;

        // By default, use the LabelSet of the main scope.
        labelSet = Scope.main.labelSenses;
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
                if(argumentCounter % 2 == 0)
                    addDependency(startPosition, endPosition);
                else
                    labelLastDependency(startPosition, endPosition);

                break;
        }

        // Increase the argument counter by one
        argumentCounter ++;
    }

    public boolean execute() {
        if(error)
            return false; // TODO: maybe give an error message?

        int amountOfDependencies;
        Sense[] types;

        // Create a new array from the ArrayList
        if(dependencies == null) {
            amountOfDependencies = 0;
            types = null;
        }
        else {
            amountOfDependencies = dependencies == null ? 0 : dependencies.size();
            types = dependencies.toArray(new Sense[amountOfDependencies]);
        }

        // Create the relation and give it a label
        Relation relation = (new Relation(type, types))
                .setLabel(label);

        /* Automatically create a rule for this expression of the form:
         *
         *      label(dependency 1, dependency 2, ... , dependency n)
         *
         */

        // TODO: create a separate method for this
        Sense[] placeholders = new Sense[amountOfDependencies];
        for(int i = 0; i < amountOfDependencies; i ++)
            placeholders[i] = Placeholder.create(types[i]);

        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token.StringToken(label));

        if(amountOfDependencies > 0) {
            tokens.add(new Token.CharToken(CHARACTER_START_ARGUMENTS));

            for (int i = 0; i < amountOfDependencies; i++) {
                tokens.add(new Token.SenseToken(placeholders[i]));
                if (i < amountOfDependencies - 1)
                    tokens.add(new Token.CharToken(CHARACTER_ARGUMENTS_SEPARATOR));
            }

            tokens.add(new Token.CharToken(CHARACTER_END_ARGUMENTS));
        }

        Expression expression = new Expression(tokens);

        Sense sense = Sense.create(relation, placeholders);
        Rule.addRule(expression, sense);
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
        dependency = (new ExpressionParser(lexer.createArray(startPosition, endPosition))).parse();
        if(dependency == null) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Was not able to parse the provided argument");
            error = true;
            return;
        }

        // Check if the parsed Sense is actually a type
        if(dependency.relation.getType() != Constant.TYPE_TYPE) { // TODO: or a child of TYPE_TYPE
            lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Expected a type");
            error = true;
            return;
        }

        // Add the type to the list of dependencies
        if(dependencies == null)
            dependencies = new ArrayList<>();
        dependencies.add(dependency);
    }

    private void labelLastDependency(int startPosition, int endPosition) {
        String label = lexer.createString(startPosition, endPosition);
        // TODO: check if valid label

        if(labelSet == Scope.main.labelSenses)
            labelSet = new LabelSet<Sense>().addParent(Scope.main.labelSenses);

//        labelSet.put(label, ~); Yeah?
    }
}
