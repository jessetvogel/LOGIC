package nl.jessevogel.logic.interpreter;

import nl.jessevogel.logic.log.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Scanner {

    private final String filename;
    private String fileContents;
    private int fileLength;
    private static final char CHARACTER_NEWLINE = '\n';

    private int pointer, line, column;
    private char currentChar;

    Scanner(String filename) {
        // Store the filename
        this.filename = filename;
    }

    boolean scan() {
        if(fileContents != null) {
            // If already scanned, give a warning and stop
            Log.warning("Scanner.scan() called while already scanned");
            return false;
        }

        try {
            // Open file for reading
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));

            // Store the entire file in one string
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(CHARACTER_NEWLINE);
            }

            // Create string
            fileContents = sb.toString();
            fileLength = fileContents.length();

            // Close file
            br.close();
            return true;
        }
        catch(IOException e) {
            // TODO: maybe we must do something here
            e.printStackTrace();
            return false;
        }
    }

    int getLine() {
        // Return the current line number
        return line;
    }

    int getColumn() {
        // Return the current column number
        return column;
    }

    char firstCharacter() {
        if(fileContents == null) {
            // If not yet scanned, give a warning, and then scan
            Log.warning("Scanner.firstCharacter() was called, but there was not yet scanned");
            scan();
        }

        // Set pointer to beginning, and return the first character
        pointer = 0;
        line = 1;
        column = 0;
        currentChar = fileContents.charAt(pointer);
        return currentChar;
    }

    char nextCharacter() {
        // If we already reached the end of the file, return 0
        if(reachedEnd()) return 0;

        // Update pointers and line numbers
        pointer ++;
        if(reachedEnd()) return 0;
        if(currentChar == CHARACTER_NEWLINE) {
            line ++;
            column = 0;
        }
        else {
            column ++;
        }

        // Get new character and return it
        currentChar = fileContents.charAt(pointer);
        return currentChar;
    }

    boolean reachedEnd() {
        return pointer == fileLength;
    }
}
