package nl.jessevogel.logic.main;

import nl.jessevogel.logic.log.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Type extends Term {

    public static Type OBJECT;
    public static Type TYPE;

    public static HashMap<String, Type> types = new HashMap<String, Type>();

    public String label;
    public ArrayList<Type> parents;
    public ArrayList<Property> properties;

    public Type() {
        super(TYPE);
        parents = new ArrayList<Type>();
        properties = new ArrayList<Property>();
    }

    public Type setLabel(String label) {
        if(this.label != null) {
            Log.warning("Resetting label of type '" + this.label + "' to '" + label + "'");
        }

        this.label = label;
        return this;
    }

    public Type getType(String label) {
        Type type = types.get(label);
        if(type == null) {
            Log.error("No type '" + label + "' found");
            return null;
        }
        else {
            return type;
        }
    }

    public Type addParentType(Type parent) {
        if(parents.contains(parent))
            Log.warning("Type '" + parent.label + "' is already a parent of type '" + label + "'");
        else
            parents.add(parent);

        return this;
    }

    public Type addProperty(Property property) {
        if(properties.contains(property))
            Log.warning("'" + property.label + "' is already a property of type '" + label + "'");
        else
            properties.add(property);

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

    public static boolean addType(Type type) {
        if(types.containsKey(type.label)) {
            Log.error("Type '" + type.label + "' was already defined");
            return false;
        }
        else {
            types.put(type.label, type);
            return true;
        }
    }
}
