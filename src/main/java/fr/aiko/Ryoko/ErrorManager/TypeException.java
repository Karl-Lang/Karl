package fr.aiko.Ryoko.ErrorManager;

public class TypeException extends RyokoException {
    public TypeException(String message, int line, int position) {
        super(message, line, position);
    }
}
