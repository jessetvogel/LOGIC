package nl.jessevogel.logic.expressions;

import nl.jessevogel.logic.basic.Type;

import java.util.ArrayList;

public class Expression {

    public ArrayList<Term> terms;

    public Expression() {
        // Create new list of terms
        terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        terms.add(term);
    }

    public static class Term {

    }

    public static class TypeTerm extends Term {
        public Type type;

        public TypeTerm(Type type) {
            this.type = type;
        }
    }

    public static class StringTerm extends Term {
        public String string;

        public StringTerm(String string) {
            this.string = string;
        }
    }

}
