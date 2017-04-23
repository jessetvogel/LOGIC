package nl.jessevogel.logic.basic;

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
