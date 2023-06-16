package studio.karllang.karl.std;

import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.Value;

public class Variable {
    private final TokenType type;
    private final String name;
    private final boolean isFinal;
    private final boolean isDeclaration;
    private Value value;

    public Variable(TokenType type, String name, Value value, boolean isFinal, boolean isDeclaration) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isFinal = isFinal;
        this.isDeclaration = isDeclaration;
    }

    public TokenType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public boolean isDeclaration() {
        return isDeclaration;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
