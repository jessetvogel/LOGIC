package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.HashMap;

public class Scope {

    public static Scope main;

    public HashMap<String, Sense> labels;
    public HashMap<Sense, Referent> referents;

    public Scope() {
        // Create a map for the senses
        referents = new HashMap<>();
        labels = new HashMap<>();
    }

    public void nameSense(String label, Sense sense) {
        // Check if the label was already used
        if(labels.containsKey(label)) {
            Log.error("The label '" + label + "' is already used");
            return;
        }

        labels.put(label, sense);
    }

    public void map(Sense sense, Referent referent) {
        // Add entry to the HashMap
        referents.put(sense, referent);
    }

    public void setEqual(Sense a, Sense b) {
        map(a, referents.get(b)); // TODO: this only works for the simple example, but please change this
    }
}
