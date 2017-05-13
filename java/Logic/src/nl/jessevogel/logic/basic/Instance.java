package nl.jessevogel.logic.basic;

import nl.jessevogel.logic.log.Log;

import java.util.HashMap;

public class Instance {

    private static final HashMap<Sense, Relation> relations = new HashMap<>();

    public static Sense create(Sense type) {
        // Make sure the given sense is actually a type
        if(type.relation.getType() != Constant.TYPE_TYPE) { // TODO: note chould also be a child of TYPE_TYPE
            Log.warning("Tried to create an Instance, but given sense is not a type");
            return null;
        }

        // If not yet a Relation was created, create a new one and put it in the map
        if(!relations.containsKey(type)) {
            relations.put(type, new Relation(type, null, null));
        }

        return Sense.createUnique(relations.get(type), null);
    }
}
