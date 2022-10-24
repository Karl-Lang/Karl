package fr.aiko.Karl.ErrorManager.SyntaxError;

import fr.aiko.Karl.ErrorManager.Error;

public class SyntaxError extends Error {
    public SyntaxError(String message, String fileName, int line) {
        super("SyntaxError", message, fileName, line);
    }
}
