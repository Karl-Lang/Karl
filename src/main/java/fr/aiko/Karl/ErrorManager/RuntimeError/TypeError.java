package fr.aiko.Karl.ErrorManager.RuntimeError;

import fr.aiko.Karl.ErrorManager.Error;

public class TypeError extends Error {
    public TypeError(String message, String fileName, int line) {
        super("TypeError", message, fileName, line);
    }
}
