package me.aikoo.Karl.Interpreter.errors.RuntimeError;

import me.aikoo.Karl.Interpreter.errors.Error;

public class RuntimeError extends Error {
    public RuntimeError(String message, String fileName, int line, int position) {
        super("Runtime Error", message, fileName, line, position);
    }
}
