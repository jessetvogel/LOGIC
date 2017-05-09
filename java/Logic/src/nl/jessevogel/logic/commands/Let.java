package nl.jessevogel.logic.commands;

import nl.jessevogel.logic.basic.Referent;
import nl.jessevogel.logic.basic.Scope;
import nl.jessevogel.logic.basic.Sense;

public class Let extends Command {

    /*
        Syntax:
            Let[Type][label]
     */

    public static final String COMMAND_NAME = "Let";

    public Let() {
        // Set values belonging to this command
        amountOfArguments = 2;
        commandName = COMMAND_NAME;
        createPositionArrays();
    }

    public boolean execute() {
        // Get the Type, and check if it exists
        String typeLabel = lexer.createString(startPositions[0], endPositions[0]);
        Type type = Type.getType(typeLabel);
        if(type == null) {
            lexer.getInterpreter().error(lexer.tokenAt(startPositions[0]), "Type '" + typeLabel + "' is undefined");
            return false;
        }

        String label = lexer.createString(startPositions[1], endPositions[1]);
        Sense sense = new Sense(type.getRelation(), null);

        // Map the sense to a new referent
        Scope.main.map(sense, new Referent());

        // Map label to this sense
        Scope.main.labels.put(label, sense);

        return true;
    }
}
