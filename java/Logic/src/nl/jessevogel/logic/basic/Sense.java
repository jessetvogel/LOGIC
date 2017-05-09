package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.HashSet;
import java.util.Set;

public class Sense {

    public static Set<Sense> senses = new HashSet<>(); // TODO : ?

    public Relation relation;
    public Sense[] dependencies;

    public Sense(Relation relation, Sense[] senses) {
        // Store the Relation
        this.relation = relation;

        // Check if the correct number of senses is provided
        int sensesLength = senses == null ? 0 : senses.length;
        if(sensesLength != relation.amountOfDependencies) {
            Log.warning("Unexpected number of dependencies given, expected " + relation.amountOfDependencies + ", but given " + sensesLength);
            return;
        }

        // Copy the dependencies
        if(senses != null) {
            dependencies = new Sense[relation.amountOfDependencies];
            for (int i = 0; i < relation.amountOfDependencies; i++) {
                // TODO: Check if the senses given have the correct type, as expected by relation
//                if (!senses[i].relation.getType().isOfType(relation.dependenciesType[i])) {
//                    Log.warning("Unexpected Sense given, expected of type '" + relation.dependenciesType[i].getLabel() + "', but given of type '" + senses[i].relation.getType().getLabel() + "'");
//                    return;
//                }
                dependencies[i] = senses[i];
            }
        }
    }

}
