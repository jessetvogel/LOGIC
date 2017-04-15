package nl.jessevogel.logic.main;

public class Main {

    public static void main(String[] args) {

        // Create standard types
        Type.OBJECT = (new Type())
                .setLabel("Object");

        Type.TYPE = (new Type())
                .setLabel("Type")
                .addParentType(Type.OBJECT);

        // Needs to be done once, as Type.TYPE was null when these two types were created
        Type.TYPE.type = Type.TYPE;
        Type.OBJECT.type = Type.TYPE;

        Type.addType(Type.OBJECT);
        Type.addType(Type.TYPE);


        // Create the main scope
        Scope mainScope = new Scope();


        // ------------------------------------------------ //


        // Custom types and stuff


        Type set = (new Type())
                .setLabel("Set")
                .addParentType(Type.OBJECT)
                .addProperty(new Property("elementType", Type.TYPE));

        Term setA = new Term(set);

        mainScope.addTerm(setA);

    }
}
