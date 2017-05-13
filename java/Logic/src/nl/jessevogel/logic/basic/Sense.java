package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.HashSet;
import java.util.Set;

public class Sense {

    private static Set<Sense> set = new HashSet<>();

    public final Relation relation;
    public final Sense[] dependencies;

    private Sense(Relation relation, Sense[] senses) {
        // Store the Relation
        this.relation = relation;

        // Check if the correct number of senses is provided
        int sensesLength = senses == null ? 0 : senses.length;
        if(sensesLength != relation.amountOfDependencies) {
            Log.warning("Unexpected number of dependencies given, expected " + relation.amountOfDependencies + ", but given " + sensesLength);
            Thread.dumpStack();
            dependencies = null;
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
        } else {
            dependencies = null;
        }
    }

    public static Sense createUnique(Relation relation, Sense[] senses) {
        return new Sense(relation, senses);
    }

    public static Sense create(Relation relation, Sense[] senses) {
        // Check if there is already some Sense which has the form that we would like TODO: this can probably be optimized much much MUCH more
        for(Sense sense : set) {
            if(sense.relation == relation) {
                int i;
                for(i = 0;i < relation.amountOfDependencies;i ++)
                    if(sense.dependencies[i] != senses[i]) break;

                // If it is a complete match, just return this sense
                if(i == relation.amountOfDependencies) return sense;
            }
        }

        // If we were not able to find it, create a new one
        Sense sense = new Sense(relation, senses);
        set.add(sense);
        return sense;
    }
}
