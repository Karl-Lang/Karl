package fr.aiko.Ryoko.parser.ErrorManager;

public class TypeException extends RyokoException {
    public TypeException(String message, int line, int position) {
        super(message, line, position);
    }
}
