package nl.jessevogel.logic.debug;

import nl.jessevogel.logic.main.Property;
import nl.jessevogel.logic.main.Term;

public class Debug {

    public void dump(Term term) {
        System.out.println("Type:\n\t" + term.type.label);
        System.out.println("Properties:");
        for(Property property : term.type.properties) {

            System.out.println(property.label + "");
        }
    }

}
