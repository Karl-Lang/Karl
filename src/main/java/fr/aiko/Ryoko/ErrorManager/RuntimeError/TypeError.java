package fr.aiko.Ryoko.ErrorManager.RuntimeError;

import fr.aiko.Ryoko.ErrorManager.Error;

public class TypeError extends Error {
    public TypeError(String message, String fileName, int line) {
        super("TypeError", message, fileName, line);
    }
}
