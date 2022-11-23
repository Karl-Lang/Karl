package fr.aiko.Karl.errors.RuntimeError;

import fr.aiko.Karl.errors.Error;

public class TypeError extends Error {
    public TypeError(String message, String fileName, int line, int position) {
        super("Type Error", message, fileName, line, position);
    }
}
