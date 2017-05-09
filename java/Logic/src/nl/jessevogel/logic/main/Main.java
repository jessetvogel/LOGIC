package nl.jessevogel.logic.main;

import nl.jessevogel.logic.basic.*;
import nl.jessevogel.logic.interpreter.FileManager;

public class Main {

    public static void main(String[] args) {

        // Create standard types
        Constant.RELATION_TYPE_TYPE = (new Relation(null, null)).setLabel("Type");
        Constant.TYPE_TYPE = new Sense(Constant.RELATION_TYPE_TYPE, null);
        Constant.RELATION_TYPE_TYPE.setType(Constant.TYPE_TYPE); // TODO: find a better way to do this. However, we must do it in some ugly way, as both final things depend on each other..

        Constant.RELATION_TYPE_OBJECT = (new Relation(Constant.TYPE_TYPE, null)).setLabel("Object");
        Constant.TYPE_OBJECT = new Sense(Constant.RELATION_TYPE_OBJECT, null);

        Constant.RELATION_TYPE_PROPOSITION = (new Relation(Constant.TYPE_TYPE, null)).setLabel("Proposition");
        Constant.TYPE_PROPOSITION = new Sense(Constant.RELATION_TYPE_PROPOSITION, null);

        Constant.RELATION_TYPE_IDENTIFIER = new Relation(Constant.TYPE_TYPE, null);
        Constant.TYPE_IDENTIFIER = new Sense(Constant.RELATION_TYPE_IDENTIFIER, null);

        // Create Relations which can instantiate (as Sense) objects (of iets dergelijks, verwoording is lastig)
        Constant.RELATION_TYPE = new Relation(Constant.TYPE_TYPE, new Sense[] { Constant.TYPE_IDENTIFIER });
        Constant.RELATION_OBJECT = new Relation(Constant.TYPE_OBJECT, new Sense[] { Constant.TYPE_IDENTIFIER });
        Constant.RELATION_PROPOSITION = new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_IDENTIFIER });
        Constant.RELATION_IDENTIFIER = new Relation(Constant.TYPE_IDENTIFIER, new Sense[] { Constant.TYPE_IDENTIFIER });

        // TODO: TYPE_TYPE and TYPE_PROPOSITION are children of TYPE_OBJECT, or: all TYPE_TYPE's and TYPE_PROPOSITION's are TYPE_OBJECT's

        // Create 'true' and 'false' objects
        Constant.PROPOSITION_TRUE = new Sense(Constant.RELATION_PROPOSITION, new Sense[] { Identifier.createInstance() });
        Constant.PROPOSITION_FALSE = new Sense(Constant.RELATION_PROPOSITION, new Sense[] { Identifier.createInstance() });

        // Create logical Relations
        Constant.RELATION_NOT = (new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_PROPOSITION })).setLabel("Not");
        Constant.RELATION_OR = (new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_PROPOSITION, Constant.TYPE_PROPOSITION })).setLabel("Or");
        Constant.RELATION_AND = (new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_PROPOSITION, Constant.TYPE_PROPOSITION })).setLabel("And");

        // Create the main scope and create some referents
        Scope.main = new Scope();

        Scope.main.map(Constant.PROPOSITION_TRUE, Referent.createInstance());
        Scope.main.map(Constant.PROPOSITION_FALSE, Referent.createInstance());

        // Read main.math
        FileManager.loadRootFile();
    }
}
