package me.aikoo.Karl.Interpreter.errors.RuntimeError;

import me.aikoo.Karl.Interpreter.errors.Error;

public class TypeError extends Error {
    public TypeError(String message, String fileName, int line, int position) {
        super("Type Error", message, fileName, line, position);
    }
}
