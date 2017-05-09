package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.HashMap;

public class Relation {

    private static final HashMap<String, Relation> labels = new HashMap<>();

    private String label;
    private Sense type; // TODO: Try to make this final again
    public final int amountOfDependencies;
    public final Sense[] dependenciesType;

    public Relation(Sense type, Sense[] dependenciesType) {
        // Make sure that 'type' is a type
        if(type.relation.getType() != Constant.TYPE_TYPE) {
            Log.warning("Tried to createInstance a Relation but the provided Sense is not a type");
            this.type = null;
            this.amountOfDependencies = 0;
            this.dependenciesType = null;
            return;
        }

        // Store all data
        this.type = type;
        this.amountOfDependencies = (dependenciesType == null ? 0 : dependenciesType.length);
        this.dependenciesType = dependenciesType;
    }

    public Relation setLabel(String label) {
        // Check if the label has already been set
        if(this.label != null) {
            Log.warning("Label of Relation '" + this.label + "' was already set");
            return this;
        }

        // Check if the label is already used
        if(labels.containsKey(label)) {
            Log.warning("Label '" + label + "' is already used");
            return this;
        }

        // Set label, and add it to the HashMap
        this.label = label;
        labels.put(label, this);
        return this;
    }

    public Sense getType() {
        // Return type
        return type;
    }

    public static boolean exists(String label) {
        return labels.containsKey(label);
    }

    public void setType(Sense type) {
        // The ONLY TIME this may be used is when we pass Constant.TYPE_TYPE
        if(type != Constant.TYPE_TYPE) {
            Log.warning("Relation.setType was called, while it may only be used for Constant.TYPE_TYPE");
            return;
        }

        this.type = type;
    }
}
