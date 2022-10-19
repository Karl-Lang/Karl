package fr.aiko.Ryoko.ErrorManager.SyntaxError;

import fr.aiko.Ryoko.ErrorManager.Error;

public class SyntaxError extends Error {
    public SyntaxError(String message, String fileName, int line) {
        super("SyntaxError", message, fileName, line);
    }
}
