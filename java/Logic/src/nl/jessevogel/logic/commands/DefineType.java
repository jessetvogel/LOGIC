package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Scope;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.basic.Relation;
import nl.jessevogel.logic.basic.Type;
import nl.jessevogel.logic.log.Log;

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
            Log.warning("Type '" + label + "' was already defined"); // TODO: replace all Log.warning(x)'s with something like interpreter.error(x), which then automatically outputs the file and line number at which the error occurs
            return false;
        }

        // Check if the label has the correct type name pattern
        if(!patternTypeName.matcher(label).find()) {
            Log.warning("Type label may only contain alphanumerical characters");
            return false;
        }

        // Define a new Type and create a corresponding Relation
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