package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Relation;
import nl.jessevogel.logic.interpreter.Token;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DefineRelation extends Command {

    /*
        Syntax:
            DefineRelation[label][Type][Type 1, Type 2, ..., Type n]
     */

    private static final String COMMAND_NAME = "DefineRelation";

    private static final Pattern patternRelationName = Pattern.compile("^[A-Za-z0-9]+$");

    public DefineRelation() {
        // Set values belonging to this command
        amountOfArguments = 3;
        commandName = COMMAND_NAME;
        createPositionArrays();
    }

    public boolean execute() {
        // Construct label for Relation
        String label = lexer.createString(startPositions[0], endPositions[0]);
        if(Relation.exists(label)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0]),"Relation '" + label + "' was already defined");
            return false;
        }

        // Check if the label has the correct type name pattern
        if(!patternRelationName.matcher(label).find()) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0]),"Relation label may only contain alphanumerical characters");
            return false;
        }

        // Find the type of the relation, and check if it exists
        String typeLabel = lexer.createString(startPositions[1], endPositions[1]);
        if(!Type.exists(typeLabel)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[1]), "Type '" + typeLabel + "' is undefined");
            return false;
        }

        // If start and end positions are the same, it has no dependencies
        Type[] dependencies = null;
        if(startPositions[2] != endPositions[2]) {
            // Construct the list of types corresponding to this Relation
            ArrayList<Type> types = new ArrayList<>();
            int position = startPositions[2];
            Token token = lexer.tokenAt(position);

            while(position < endPositions[2]) {
                if(!(token instanceof Token.StringToken)) {
                    lexer.getInterpreter().error(token, "Unexpected token, expected a Type");
                    return false;
                }
                String typesLabel = ((Token.StringToken) token).str;
                if (!Type.exists(typesLabel)) {
                    lexer.getInterpreter().error(token, "Type '" + typesLabel + "' is undefined");
                    return false;
                }
                types.add(Type.getType(typesLabel));

                if(++ position == endPositions[2]) break;

                // Expect a ',' to separate between types
                token = lexer.tokenAt(position);
                if(!(token instanceof Token.CharToken) || ((Token.CharToken) token).c != ',') {
                    lexer.getInterpreter().error(token, "Unexpected symbol, expected ','");
                    return false;
                }

                token = lexer.tokenAt(++ position);
            }

            // Create a new array from the ArrayList
            dependencies = types.toArray(new Type[types.size()]);
        }

        // Create the relation and give it a label
        (new Relation(Type.getType(typeLabel), dependencies))
                .setLabel(label);

        return true;
    }
}
