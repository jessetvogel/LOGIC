package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.interpreter.Token;
import nl.jessevogel.logic.log.Log;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class LabelSet<T> {

    private Map<String, T> labels;
    private ArrayList<LabelSet<T>> parents;

    private boolean allowOverriding;

    public LabelSet() {
        // Create a new HashMap
        labels = new HashMap<>();

        // Set default stuff
        allowOverriding = false;
    }

    public boolean put(String label, T object) {
        if(!allowOverriding && isSet(label)) {
            // If this label was already used, give a warning
            Log.warning("The label '" + label + "' was already used");
            return false;
        }

        // Associate the label with the object
        labels.put(label, object);
        return true;
    }

    public T get(String label) {
        // Return the object if it is in this LabelSet
        if(labels.containsKey(label))
            return labels.get(label);

        // Return the object if it is in some parent LabelSet
        for(LabelSet<T> parent : parents) {
            if(parent.labels.containsKey(label))
                return parent.labels.get(label);
        }

        // If not found, return null
        return null;
    }

    public boolean isSet(String label) {
        // Check if it is defined in this LabelSet
        if(labels.containsKey(label)) return true;

        // Check if it is defined in some parent LabelSet
        if(parents != null) {
            for(LabelSet<T> ls : parents)
                if(ls.isSet(label))
                    return true;
        }

        // If not, return false
        return false;
    }

    public LabelSet<T> setAllowOverriding(boolean x) {
        allowOverriding = x;
        return this;
    }

    public LabelSet<T> addParent(LabelSet<T> parent) {
        // TODO: check for, and prevent if detected, recursion

        if(parents == null) parents = new ArrayList<>();
        parents.add(parent);
        return this;
    }
}
