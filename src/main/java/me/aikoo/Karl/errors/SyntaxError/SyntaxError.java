package me.aikoo.Karl.errors.SyntaxError;

import me.aikoo.Karl.errors.Error;

public class SyntaxError extends Error {
    public SyntaxError(String message, String fileName, int line, int position) {
        super("Syntax Error", message, fileName, line, position);
    }
}
