package nl.jessevogel.logic.main;

import nl.jessevogel.logic.log.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Scope {

    public HashMap<Sense, Referent> referents;

    public Scope() {
        // Create a map for the senses
        referents = new HashMap<Sense, Referent>();
    }

    public void map(Sense sense, Referent referent) {
        // Add entry to the HashMap
        referents.put(sense, referent);
    }

    public void setEqual(Sense a, Sense b) {
        map(a, referents.get(b)); // TODO: this only works for the simple example, but please change this
    }
}
