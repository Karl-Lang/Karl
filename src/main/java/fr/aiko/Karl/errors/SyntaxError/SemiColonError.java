package fr.aiko.Karl.errors.SyntaxError;

public class SemiColonError extends SyntaxError {
    public SemiColonError(String fileName, int line) {
        super("Missing semi colon", fileName, line);
    }
}
