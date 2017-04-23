package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.HashMap;

public class Relation {

    public static Relation NOT; // TODO: not sure if this is even necessary? Maybe it is sufficient to just have a reference in the labels
    public static Relation OR;
    public static Relation AND;

    private static final HashMap<String, Relation> labels = new HashMap<>();

    private String label;
    private final Type type;
    public final int amountOfDependencies;
    public final Type[] dependenciesType;

    public Relation(Type type, Type[] dependenciesType) {
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

    public Type getType() {
        // Return type
        return type;
    }

    public static boolean exists(String label) {
        return labels.containsKey(label);
    }
}
