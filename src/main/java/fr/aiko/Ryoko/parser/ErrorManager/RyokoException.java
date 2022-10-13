package fr.aiko.Ryoko.parser.ErrorManager;

public abstract class RyokoException extends RuntimeException {
    private final String message;
    private final int line;
    private final int position;

    public RyokoException(String message, int line, int position) {
        this.message = message;
        this.line = line;
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return position;
    }
}
