package nl.jessevogel.logic.expressions;

import java.util.regex.Pattern;

public class Label {

    private static final Pattern patternLabel = Pattern.compile("^[A-Za-z0-9]+$");

    public static boolean valid(String label) {
        return patternLabel.matcher(label).find();
    }
}
