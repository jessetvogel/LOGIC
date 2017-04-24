package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.basic.Type;
import nl.jessevogel.logic.log.Log;

public class SetParentType extends Command {

    /*
        Syntax:
            SetParentType[Type child][Type parent]
     */

    public static final String COMMAND_NAME = "SetParentType";

    public SetParentType() {
        // Set values belonging to this command
        amountOfArguments = 2;
        commandName = COMMAND_NAME;
        createPositionArrays();
    }

    public boolean execute() {
        // Get the child and parent labels, and check if they exist
        String childTypeLabel = lexer.createString(startPositions[0], endPositions[0]);
        Type childType = Type.getType(childTypeLabel);
        if(childType == null) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0]), "Type '" + childTypeLabel + "' is undefined");
            return false;
        }

        String parentTypeLabel = lexer.createString(startPositions[1], endPositions[1]);
        Type parentType = Type.getType(parentTypeLabel);
        if(parentType == null) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[1]), "Type '" + parentTypeLabel + "' is undefined");
            return false;
        }

        // Check if the child is not already a parent of the parent (then we create an vicious circle)
        if(parentType.isOfType(childType)) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0] - 2), "Tried to set '" + parentTypeLabel + "' as parent of '" + childTypeLabel + "' while it is already its child"); // TODO: use better method to find correct token
            return false;
        }

        // Add parent type
        childType.addParentType(parentType);
        return true;
    }

}
