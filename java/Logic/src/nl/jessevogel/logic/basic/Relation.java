package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.expressions.LabelSet;
import nl.jessevogel.logic.log.Log;

import java.util.HashMap;

public class Relation {

    private static final LabelSet<Relation> labelSet = new LabelSet<>();

    private String label;
    private final Sense type;
    public final Sense[] placeholders;
    public final Sense[] types;
    public final int amountOfDependencies;

    public Relation(Sense type, Sense[] placeholders, Sense[] types) {
        // TODO: there should be a better way to do this, but it works, so hey!
        if(Constant.TYPE_TYPE == null) {
            type = Constant.TYPE_TYPE = Sense.create(this, null);
        }
        else {
            // Make sure that 'type' is a type
            if (type.relation.getType() != Constant.TYPE_TYPE) { // TODO: or some child of TYPE_TYPE
                Log.warning("Tried to create a Relation but the provided Sense is not a type");
                this.type = null;
                this.placeholders = null;
                this.types = null;
                this.amountOfDependencies = 0;
                return;
            }
        }

        // Store all data
        this.type = type;
        this.amountOfDependencies = (types == null ? 0 : types.length);
        this.placeholders = placeholders;
        this.types = types;
    }

    public Relation setLabel(String label) {
        // Check if the label has already been set
        if(this.label != null) {
            Log.warning("Call of Relation '" + this.label + "' was already set");
            return this;
        }

        // Try to set the label
        if(labelSet.put(label, this))
            this.label = label;

        return this;
    }

    public String getLabel() {
        return label;
    }

    public Sense getType() {
        // Return type
        return type;
    }

    public static boolean exists(String label) {
        return labelSet.isSet(label);
    }
}
