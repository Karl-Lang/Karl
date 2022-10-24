package fr.aiko.Karl.ErrorManager.RuntimeError;

import fr.aiko.Karl.ErrorManager.Error;

public class RuntimeError extends Error {
    public RuntimeError(String message, String fileName, int line) {
        super("RuntimeError", message, fileName, line);
    }
}
