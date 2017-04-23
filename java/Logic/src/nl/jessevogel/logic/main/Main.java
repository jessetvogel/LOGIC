package nl.jessevogel.logic.main;

import nl.jessevogel.logic.basic.*;
import nl.jessevogel.logic.interpreter.FileManager;
import nl.jessevogel.logic.interpreter.Interpreter;

import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        // Initialize some stuff
        Sense.senses = new HashSet<Sense>();

        // Create standard types
        Type.OBJECT = (new Type())
                .setLabel("Object");

        Type.TYPE = (new Type())
                .setLabel("Type")
                .addParentType(Type.OBJECT);

        // Create SenseTypes for types Object and Type
        SenseType stObject = new SenseType(Type.OBJECT, 0, null);
        SenseType stType = new SenseType(Type.OBJECT, 0, null);

        Type.addType(Type.OBJECT);
        Type.addType(Type.TYPE);


        // Create the main scope
        Scope mainScope = new Scope();


        // ------------------------------------------------ //

        // Semi-custom stuff
        Type proposition = (new Type())
                .setLabel("Proposition")
                .addParentType(Type.OBJECT);

        Type.addType(proposition);

        SenseType stProposition = new SenseType(proposition, 0, null);

        // Create TRUE and FALSE objects
        Sense TRUE = new Sense(stProposition, null);
        Sense FALSE = new Sense(stProposition, null);
        mainScope.map(TRUE, new Referent());
        mainScope.map(FALSE, new Referent());

        // Define (partially) boolean algebra
        SenseType and = new SenseType(proposition, 2, new Type[] { proposition, proposition });
        SenseType or = new SenseType(proposition, 2, new Type[] { proposition, proposition });
        SenseType not = new SenseType(proposition, 1, new Type[] { proposition });
        // etc.

        Sense AND1 = new Sense(and, new Sense[] {TRUE, TRUE}); mainScope.setEqual(AND1, TRUE);
        Sense AND2 = new Sense(and, new Sense[] {TRUE, FALSE}); mainScope.setEqual(AND2, FALSE);
        Sense AND3 = new Sense(and, new Sense[] {FALSE, TRUE}); mainScope.setEqual(AND3, FALSE);
        Sense AND4 = new Sense(and, new Sense[] {FALSE, FALSE}); mainScope.setEqual(AND4, FALSE);

        Sense OR1 = new Sense(or, new Sense[] {TRUE, TRUE}); mainScope.setEqual(OR1, TRUE);
        Sense OR2 = new Sense(or, new Sense[] {TRUE, FALSE}); mainScope.setEqual(OR2, TRUE);
        Sense OR3 = new Sense(or, new Sense[] {FALSE, TRUE}); mainScope.setEqual(OR3, TRUE);
        Sense OR4 = new Sense(or, new Sense[] {FALSE, FALSE}); mainScope.setEqual(OR4, FALSE);

        Sense NOT1 = new Sense(not, new Sense[] {TRUE}); mainScope.setEqual(NOT1, FALSE);
        Sense NOT2 = new Sense(not, new Sense[] {FALSE}); mainScope.setEqual(NOT2, TRUE);

        // ------------------------------------------------ //


        // Custom types and stuff
        Type set = (new Type())
                .setLabel("Set")
                .addParentType(Type.OBJECT);

        Type.addType(set);

        SenseType stSet = new SenseType(set, 0, null);

        Sense A = new Sense(stSet, null);
        mainScope.map(A, new Referent());

        SenseType stIn = new SenseType(proposition, 2, new Type[] { Type.OBJECT, set});


        // ------------------------------------------------ //

        // Read main.math
        FileManager.loadRootFile();


        // ------------------------------------------------ //


        // Some tests


    }
}
