package fr.aiko.Karl.ErrorManager.SyntaxError;

public class SemiColonError extends SyntaxError {
    public SemiColonError(String fileName, int line) {
        super("Missing semi colon", fileName, line);
    }
}
