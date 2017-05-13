package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.basic.Relation;
import nl.jessevogel.logic.basic.Scope;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.expressions.*;
import nl.jessevogel.logic.expressions.Label;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Define extends Command {

    /*
        Syntax:
            Define[label][type][type 1][label 1][type 2][label 2] ... [type n][label n]
     */

    private static final String COMMAND_NAME = "Define";
    private static final char CHARACTER_START_ARGUMENTS = '(';
    private static final char CHARACTER_END_ARGUMENTS = ')';
    private static final char CHARACTER_ARGUMENTS_SEPARATOR = ',';
    private static final char CHARACTER_NO_LABEL = '~';

    private int argumentCounter;
    private String label;
    private ArrayList<Token> typeTokens;
    private ArrayList<Sense> dependencyPlaceholders;
    private ArrayList<Sense> dependencyTypes;
    private LabelSet<Sense> labelSet;
    private Sense lastDependencyType;

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
        if(error) return;

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
                    labelDependency(startPosition, endPosition);

                break;
        }

        // Increase the argument counter by one
        argumentCounter ++;
    }

    public boolean execute() {
        if(error)
            return false;

        if(argumentCounter % 2 != 0) {
            lexer.getInterpreter().error(firstToken, "Expected a label for given type");
            return false;
        }

        // Prepare to define a new Relation
        int amountOfDependencies;
        Sense[] placeholders;
        Sense[] types;
        if(dependencyTypes == null) {
            amountOfDependencies = 0;
            placeholders = null;
            types = null;
        }
        else {
            amountOfDependencies = dependencyTypes.size();
            placeholders = dependencyPlaceholders.toArray(new Sense[amountOfDependencies]);
            types = dependencyTypes.toArray(new Sense[amountOfDependencies]);
        }

        // Determine the type at last, because it may refer to some of the arguments
        Sense type = (new ExpressionParser(labelSet.substituteTokens(typeTokens))).parse();
        if(type == null) {
            lexer.getInterpreter().error(typeTokens.get(0), "Was not able to parse the provided argument");
            return false;
        }

        // Check if the parsed Sense is actually a type
        if(type.relation.getType() != Constant.TYPE_TYPE) {
            lexer.getInterpreter().error(typeTokens.get(0), "Expected a type");
            return false;
        }

        // Create the relation and give it a label
        Relation relation = (new Relation(type, placeholders, types))
                .setLabel(label);

        /* Automatically create a rule for this expression of the form:
         *
         *      label(dependency 1, dependency 2, ... , dependency n)
         *
         */

        // TODO: create a separate method for this, somewhere in some Expression type of class
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token.StringToken(label));

        if(amountOfDependencies > 0) {
            tokens.add(new Token.CharToken(CHARACTER_START_ARGUMENTS));

            for(int i = 0; i < amountOfDependencies; i++) {
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
        if(!Label.valid(label)) {
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
        typeTokens = lexer.createArray(startPosition, endPosition);
    }

    private void addDependency(int startPosition, int endPosition) {
        // Parse the sense
        lastDependencyType = (new ExpressionParser(labelSet.substituteTokens(lexer.createArray(startPosition, endPosition)))).parse();
        if(lastDependencyType == null) {
            lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Was not able to parse the provided argument");
            error = true;
            return;
        }

        // Check if the parsed Sense is actually a type
        if(lastDependencyType.relation.getType() != Constant.TYPE_TYPE) { // TODO: or a child of TYPE_TYPE
            lexer.getInterpreter().error(lexer.tokenAt(startPosition), "Expected a type");
            error = true;
            return;
        }

        // Add the type to the list of dependencies
        if(dependencyTypes == null) {
            dependencyTypes = new ArrayList<>();
            dependencyPlaceholders = new ArrayList<>();
        }
        dependencyTypes.add(lastDependencyType);
    }

    private void labelDependency(int startPosition, int endPosition) {
        // Add a new placeholder to the list of placeholders
        Sense placeholder = Placeholder.create(lastDependencyType);
        dependencyPlaceholders.add(placeholder);

        // Give the placeholder a label if asked for
        String label = lexer.createString(startPosition, endPosition);
        if(label.length() == 1 && label.charAt(0) == CHARACTER_NO_LABEL) return;

        // TODO: check if valid label

        // Create new LabelSet if not yet done
        if(labelSet == Scope.main.labelSenses)
            labelSet = new LabelSet<Sense>().addParent(Scope.main.labelSenses);

        labelSet.put(label, placeholder);
    }
}
