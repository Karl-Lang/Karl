package me.aikoo.Karl.errors.RuntimeError;

import me.aikoo.Karl.errors.Error;

public class TypeError extends Error {
    public TypeError(String message, String fileName, int line, int position) {
        super("Type Error", message, fileName, line, position);
    }
}
