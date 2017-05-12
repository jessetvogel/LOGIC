package nl.jessevogel.logic.main;

import nl.jessevogel.logic.basic.*;
import nl.jessevogel.logic.expressions.Expression;
import nl.jessevogel.logic.expressions.Rule;
import nl.jessevogel.logic.interpreter.FileManager;
import nl.jessevogel.logic.interpreter.Token;

public class Main {

    public static void main(String[] args) {

        // Create standard types
        Constant.RELATION_TYPE_TYPE = (new Relation(null, null)).setLabel("Type"); // Note: this also sets Constant.TYPE_TYPE (see Relation.java)

        Constant.RELATION_TYPE_OBJECT = (new Relation(Constant.TYPE_TYPE, null)).setLabel("Object");
        Constant.TYPE_OBJECT = Sense.create(Constant.RELATION_TYPE_OBJECT, null);

        Constant.RELATION_TYPE_PROPOSITION = (new Relation(Constant.TYPE_TYPE, null)).setLabel("Proposition");
        Constant.TYPE_PROPOSITION = Sense.create(Constant.RELATION_TYPE_PROPOSITION, null);

        // Create Relations which can instantiate (as Sense) objects (of iets dergelijks, verwoording is lastig)
        Constant.RELATION_TYPE = new Relation(Constant.TYPE_TYPE, new Sense[] { Constant.TYPE_IDENTIFIER });
        Constant.RELATION_OBJECT = new Relation(Constant.TYPE_OBJECT, new Sense[] { Constant.TYPE_IDENTIFIER });
        Constant.RELATION_PROPOSITION = new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_IDENTIFIER });

        // TODO: TYPE_TYPE and TYPE_PROPOSITION are children of TYPE_OBJECT, or: all TYPE_TYPE's and TYPE_PROPOSITION's are TYPE_OBJECT's

        // Create 'true' and 'false' objects
        Constant.PROPOSITION_TRUE = Instance.create(Constant.TYPE_PROPOSITION);
        Constant.PROPOSITION_FALSE = Instance.create(Constant.TYPE_PROPOSITION);

        // Create logical Relations
        Constant.RELATION_NOT = (new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_PROPOSITION })).setLabel("Not");
        Constant.RELATION_OR = (new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_PROPOSITION, Constant.TYPE_PROPOSITION })).setLabel("Or");
        Constant.RELATION_AND = (new Relation(Constant.TYPE_PROPOSITION, new Sense[] { Constant.TYPE_PROPOSITION, Constant.TYPE_PROPOSITION })).setLabel("And");

        // Create the main scope and create some referents
        Scope.main = new Scope();

        Scope.main.map(Constant.PROPOSITION_TRUE, Referent.createInstance());
        Scope.main.map(Constant.PROPOSITION_FALSE, Referent.createInstance());



        // Create rules associated to the above defined things
        Rule.addRule(new Expression(new Token[] { new Token.StringToken("Type") }), Constant.TYPE_TYPE);
        Rule.addRule(new Expression(new Token[] { new Token.StringToken("Object") }), Constant.TYPE_OBJECT);
        Rule.addRule(new Expression(new Token[] { new Token.StringToken("Proposition") }), Constant.TYPE_PROPOSITION);

        Rule.addRule(new Expression(new Token[] { new Token.StringToken("true") }), Constant.PROPOSITION_TRUE);
        Rule.addRule(new Expression(new Token[] { new Token.StringToken("false") }), Constant.PROPOSITION_FALSE);

        // Read main.math
        FileManager.loadRootFile();
    }
}
