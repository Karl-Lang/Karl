package me.aikoo.Karl.errors.RuntimeError;

import me.aikoo.Karl.errors.Error;

public class NumberError extends Error {
    public NumberError(String message, String fileName, int line, int position) {
        super("Number Error", message, fileName, line, position);
    }
}
