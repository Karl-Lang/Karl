package me.aikoo.Karl.errors.RuntimeError;

import me.aikoo.Karl.errors.Error;

public class RuntimeError extends Error {
    public RuntimeError(String message, String fileName, int line, int position) {
        super("Runtime Error", message, fileName, line, position);
    }
}
