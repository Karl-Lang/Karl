package fr.aiko.Karl.errors.RuntimeError;

import fr.aiko.Karl.errors.Error;

public class NumberError extends Error {
    public NumberError(String message, String fileName, int line, int position) {
        super("Number Error", message, fileName, line, position);
    }
}
