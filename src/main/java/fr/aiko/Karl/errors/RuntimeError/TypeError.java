package fr.aiko.Karl.errors.RuntimeError;

import fr.aiko.Karl.errors.Error;

public class TypeError extends Error {
    public TypeError(String message, String fileName, int line) {
        super("TypeError", message, fileName, line);
    }
}
