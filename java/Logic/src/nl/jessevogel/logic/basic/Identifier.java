package nl.jessevogel.logic.basic;

public class Identifier {

    public static Sense createInstance() {
        // Return a new Identifier
        return new Sense(Constant.RELATION_IDENTIFIER, null); // TODO: what does an identifier depend on?
    }
}
