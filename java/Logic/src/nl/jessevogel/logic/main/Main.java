package nl.jessevogel.logic.main;

import nl.jessevogel.logic.basic.*;
import nl.jessevogel.logic.interpreter.FileManager;

public class Main {

    public static void main(String[] args) {

        // Create standard labels
        Type.OBJECT = new Type();
        Type.TYPE = new Type();
        Type.PROPOSITION = new Type();

        Relation relationObject = new Relation(Type.OBJECT, null);
        Relation relationType = new Relation(Type.TYPE, null);
        Relation relationProposition = new Relation(Type.PROPOSITION, null);

        Type.OBJECT
                .setLabel("Object")
                .setRelation(relationObject);

        Type.TYPE
                .setLabel("Type")
                .addParentType(Type.OBJECT)
                .setRelation(relationType);

        Type.PROPOSITION
                .setLabel("Proposition")
                .addParentType(Type.OBJECT)
                .setRelation(relationProposition);

        // Create 'true' and 'false' objects
        Sense.TRUE = new Sense(relationProposition, null);
        Sense.FALSE = new Sense(relationProposition, null);

        // Create the main scope and set some labels and referents
        Scope.main = new Scope();

        Scope.main.nameSense(Type.OBJECT.getLabel(), new Sense(Type.TYPE.getRelation(),null));
        Scope.main.nameSense(Type.TYPE.getLabel(), new Sense(Type.TYPE.getRelation(),null));
        Scope.main.nameSense(Type.PROPOSITION.getLabel(), new Sense(Type.TYPE.getRelation(),null));

        Scope.main.map(Sense.TRUE, new Referent());
        Scope.main.map(Sense.FALSE, new Referent());

        Scope.main.nameSense("true", Sense.TRUE);
        Scope.main.nameSense("false", Sense.FALSE);

        // Define (partially) boolean algebra
        Relation.NOT = (new Relation(Type.PROPOSITION, new Type[] { Type.PROPOSITION }))
                .setLabel("not");
        Relation.OR = (new Relation(Type.PROPOSITION, new Type[] { Type.PROPOSITION, Type.PROPOSITION }))
                .setLabel("or");
        Relation.AND = (new Relation(Type.PROPOSITION, new Type[] { Type.PROPOSITION, Type.PROPOSITION }))
                .setLabel("and");

        // Read main.math
        FileManager.loadRootFile();
    }
}
