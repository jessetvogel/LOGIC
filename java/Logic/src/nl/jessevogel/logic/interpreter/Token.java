package nl.jessevogel.logic.interpreter;

public class Token {

    public static class CharToken extends Token {

        public final char c;

        public CharToken(char c) {
            // Store char
            this.c = c;
        }
    }

    public static class StringToken extends Token {

        public final String str;

        public StringToken(String str) {
            // Store string
            this.str = str;
        }
    }
}
