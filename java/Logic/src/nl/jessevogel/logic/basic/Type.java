package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Type {

    public static Type OBJECT;
    public static Type TYPE;
    public static Type PROPOSITION;

    public static HashMap<String, Type> labels = new HashMap<>();

    private String label;
    private ArrayList<Type> parents;
    private Relation senseType;

    public Type() {
        // Create an empty list of parents
        parents = new ArrayList<>();
    }

    public Type setLabel(String label) {
        // Check if the label has already been set
        if(this.label != null) {
            Log.warning("Label of Type '" + this.label + "' was already set");
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

    public String getLabel() {
        // Return label
        return label;
    }

    public Type setRelation(Relation senseType) {
        // If already set, give a warning
        if(this.senseType != null)
            Log.warning("Setting Relation of Type '" + label + "', while it was already set");

        this.senseType = senseType;
        return this;
    }

    public Relation getRelation() {
        // Return the relation
        return senseType;
    }

    public Type addParentType(Type parent) {
        if(parents.contains(parent))
            Log.warning("Type '" + parent.label + "' is already a parent of type '" + label + "'");
        else
            parents.add(parent);

        return this;
    }

    public boolean isOfType(Type type) {
        if(type == this)
            return true;

        for(Type parent : parents) {
            if(parent.isOfType(type))
                return true;
        }

        return false;
    }

    public static boolean exists(String label) {
        return labels.containsKey(label);
    }

    public static Type getType(String label) {
        Type type = labels.get(label);
        if(type == null) {
            Log.error("No type '" + label + "' found");
            return null;
        }
        else {
            return type;
        }
    }
}
