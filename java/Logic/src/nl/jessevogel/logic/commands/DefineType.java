package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Scope;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.basic.Relation;

import java.util.regex.Pattern;

public class DefineType extends Command {

    /*
        Syntax:
            DefineType[label]
     */

    private static final String COMMAND_NAME = "DefineType";

    private static final Pattern patternTypeName = Pattern.compile("^[A-Za-z0-9]+$");

    public DefineType() {
        // Set values belonging to this command
        amountOfArguments = 1;
        commandName = COMMAND_NAME;
        createPositionArrays();
    }

    public boolean execute() {
        // Construct label for Type
        String label = lexer.createString(startPositions[0], endPositions[0]);
        if(Type.exists(label)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0]), "Type '" + label + "' was already defined");
            return false;
        }

        // Check if the label has the correct type name pattern
        if(!patternTypeName.matcher(label).find()) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0]), "Type label may only contain alphanumerical characters");
            return false;
        }

        // Define a new Type and createInstance a corresponding Relation
        // Also, everything is by default an Object
        Type type = new Type();
        type
                .setLabel(label)
                .setRelation(new Relation(type, null))
                .addParentType(Type.OBJECT);

        Sense sense = new Sense(Type.TYPE.getRelation(), null);
        Scope.main.nameSense(label, sense);
        return true;
    }
}
