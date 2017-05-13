package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class LabelSet<T> {

    private Map<String, T> labels;
    private ArrayList<LabelSet<T>> parents;

    public LabelSet() {
        // Create a new HashMap
        labels = new HashMap<>();
    }

    public boolean put(String label, T object) {
        // Check if the label is already used
        if(isSet(label)) {
            Log.warning("The label '" + label + "' was already used");
            return false;
        }

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
            for(LabelSet<T> labelSet : parents)
                if(labelSet.isSet(label))
                    return true;
        }

        // If not, return false
        return false;
    }

    public LabelSet<T> addParent(LabelSet<T> parent) {
        // Check for recursion
        if(parent.isParent(this)) {
            Log.warning("LabelSet is already parent of its parent");
            return this;
        }

        if(parents == null) parents = new ArrayList<>();
        parents.add(parent);
        return this;
    }

    private boolean isParent(LabelSet<T> labelSet) {
        if(labelSet == this) return true;
        if(parents == null) return false;
        for(LabelSet<T> l : parents)
            if(l.isParent(labelSet))
                return true;
        return false;
    }
}
