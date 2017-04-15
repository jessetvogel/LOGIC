package nl.jessevogel.logic.main;

import nl.jessevogel.logic.log.Log;

import java.util.HashSet;
import java.util.Set;

public class Scope {

    public Set<Term> terms;

    public Scope() {

        // Create a set of terms
        terms = new HashSet<Term>();

    }

    public boolean addTerm(Term term) {
        if(terms.contains(term)) {
            Log.error("Trying to add a term to scope which was already added");
            return false;
        }
        else  {
            terms.add(term);
            return true;
        }
    }
}
