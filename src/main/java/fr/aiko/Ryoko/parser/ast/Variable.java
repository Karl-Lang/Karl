package fr.aiko.Ryoko.parser.ast;

public class Variable {
    private final String name;
    private final boolean isFinal;
    private final String type;
    private String value;

    public Variable(String type, String name, String value, boolean isFinal) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (!isFinal) this.value = value;
        else throw new RuntimeException("Cannot assign a value to a final variable");
    }

    public String getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
