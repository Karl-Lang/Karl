package me.aikoo.Karl.Interpreter.errors.SyntaxError;

public class SemiColonError extends SyntaxError {
    public SemiColonError(String fileName, int line, int position) {
        super("Missing semi colon", fileName, line, position);
    }
}
