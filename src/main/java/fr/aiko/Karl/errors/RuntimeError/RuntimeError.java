package fr.aiko.Karl.errors.RuntimeError;

import fr.aiko.Karl.errors.Error;

public class RuntimeError extends Error {
    public RuntimeError(String message, String fileName, int line) {
        super("RuntimeError", message, fileName, line);
    }
}
