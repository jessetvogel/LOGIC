package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.Set;

public class Sense {

    public static Set<Sense> senses;

    public SenseType senseType;
    public Sense[] dependencies;

    public Sense(SenseType senseType, Sense[] senses) {
        // Copy the SenseType
        this.senseType = senseType;

        // Check if the correct number of senses is provided
        int sensesLength = senses == null ? 0 : senses.length;
        if(sensesLength != senseType.amountOfDependencies) {
            Log.warning("Unexpected number of dependencies given, expected " + senseType.amountOfDependencies + ", but given " + sensesLength);
            return;
        }

        // Copy the dependencies
        if(senses != null) {
            dependencies = new Sense[senseType.amountOfDependencies];
            for (int i = 0; i < senseType.amountOfDependencies; i++) {
                // Check if the senses given have the correct type, as expected by senseType
                if (!senses[i].senseType.type.isOfType(senseType.dependenciesType[i])) {
                    Log.warning("Unexpected Sense given, expected of type '" + senseType.dependenciesType[i].label + "', but given of type '" + senses[i].senseType.type.label + "'");
                    return;
                }
                dependencies[i] = senses[i];
            }
        }
    }

}
