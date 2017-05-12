package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Constant;
import nl.jessevogel.logic.basic.Relation;
import nl.jessevogel.logic.basic.Sense;
import nl.jessevogel.logic.log.Log;

import java.util.HashMap;
import java.util.Map;

public class Placeholder {

    private static final HashMap<Sense, Relation> relations = new HashMap<>();

    public static Sense create(Sense type) {
        // Make sure the given sense is actually a type
        if(type.relation.getType() != Constant.TYPE_TYPE) { // TODO: note chould also be a child of TYPE_TYPE
            Log.warning("Tried to create a Placeholder, but given sense is not a type");
            return null;
        }

        // If not yet a Relation was created, create a new one and put it in the map
        if(!relations.containsKey(type)) {
            relations.put(type, new Relation(type, null));
        }

        return Sense.createUnique(relations.get(type), null);
    }

    public static Sense replace(Sense sense, Map<Sense, Sense> placeholders) {
        // In case of a placeholder
        if(Placeholder.is(sense)) {
            Sense x = placeholders.get(sense);
            if(x == null)
                Log.warning("Placeholder was not found!! :O :O "); // TODO: change the contents of this warning

            return x;
        }

        // Replace all dependencies, and create a new sense out of that
        Sense[] senses;
        if(sense.dependencies == null)
            senses = null;
        else {
            int size = sense.relation.amountOfDependencies;
            senses = new Sense[size];
            for (int i = 0; i < size; i++)
                senses[i] = replace(sense.dependencies[i], placeholders);
        }

        return Sense.create(sense.relation, senses);
    }

    public static boolean is(Sense sense) {
        // Check if the relation belonging to this sense is contained in the relations-map
        return relations.containsValue(sense.relation);
    }
}
