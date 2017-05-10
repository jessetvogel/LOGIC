package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.HashMap;

public class Relation {

    private static final HashMap<String, Relation> labels = new HashMap<>();
    private static final HashMap<Sense, Relation> types = new HashMap<>();

    private String label;
    private final Sense type;
    public final int amountOfDependencies;
    public final Sense[] dependenciesType;

    public Relation(Sense type, Sense[] dependenciesType) {
        // TODO: there should be a better way to do this, but it works, so hey!
        if(Constant.TYPE_TYPE == null) {
            type = Constant.TYPE_TYPE = new Sense(this, null);
        }
        else {
            // Make sure that 'type' is a type
            if (type.relation.getType() != Constant.TYPE_TYPE) {
                Log.warning("Tried to create a Relation but the provided Sense is not a type");
                this.type = null;
                this.amountOfDependencies = 0;
                this.dependenciesType = null;
                return;
            }
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

    public static Relation fromType(Sense type) {
        if(type.relation.getType() != Constant.TYPE_TYPE) { // TODO: note chould also be a child of TYPE_TYPE
            Log.warning("Requested Relation related to a sense that is not a type");
            return null;
        }

        Relation relation = types.get(type);
        if(relation == null) {
            // If not yet created, create a new one and put it in the map
            relation = new Relation(type, new Sense[] { Constant.TYPE_IDENTIFIER });
            types.put(type, relation);
        }
        return relation;
    }
}
