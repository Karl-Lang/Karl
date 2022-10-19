package fr.aiko.Ryoko.ErrorManager.RuntimeError;

import fr.aiko.Ryoko.ErrorManager.Error;

public class RuntimeError extends Error {
    public RuntimeError(String message, String fileName, int line) {
        super("RuntimeError", message, fileName, line);
    }
}
