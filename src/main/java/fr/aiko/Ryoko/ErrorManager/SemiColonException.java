package fr.aiko.Ryoko.ErrorManager;

public class SemiColonException extends RyokoException {
    public SemiColonException(int line, int position) {
        super("Missing semicolon at : ", line, position);
    }
}
