package nl.jessevogel.logic.main;

import java.util.HashMap;

public class SenseType {

    public final Type type;
    public final int amountOfDependencies;
    public final Type[] dependenciesType;

    public SenseType(Type type, int amountOfDependencies, Type[] dependenciesType) {
        this.type = type;
        this.amountOfDependencies = amountOfDependencies;
        this.dependenciesType = dependenciesType;
    }

}
