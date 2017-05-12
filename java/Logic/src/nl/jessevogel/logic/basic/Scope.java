package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.expressions.LabelSet;
import nl.jessevogel.logic.log.Log;

import java.util.HashMap;

public class Scope {

    public static Scope main;

    public LabelSet<Sense> labelSenses;
    public HashMap<Sense, Referent> referents;

    public Scope() {
        // Create a map for the senses
        referents = new HashMap<>();
        labelSenses = new LabelSet<>();
    }

    public void nameSense(String label, Sense sense) {
        // Try to associate the label with the sense
        labelSenses.put(label, sense);
    }

    public void map(Sense sense, Referent referent) {
        // Add entry to the HashMap
        referents.put(sense, referent);
    }

    public void setEqual(Sense a, Sense b) {
        map(a, referents.get(b)); // TODO: this only works for the simple example, but please change this
    }
}
