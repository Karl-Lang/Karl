package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.*;

public class ValueExpression extends Expression {
    private final Value value;
    private final TokenType type;

    public ValueExpression(Integer value, TokenType type) {
        this.value = new IntValue(value);
        this.type = type;
    }

    public ValueExpression(String value, TokenType type) {
        this.value = new StringValue(value);
        this.type = type;
    }

    public ValueExpression(Boolean value, TokenType type) {
        this.value = new BooleanValue(value);
        this.type = type;
    }

    public ValueExpression(Float value, TokenType type) {
        this.value = new FloatValue(value);
        this.type = type;
    }

    public ValueExpression(Value value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public ValueExpression(Character value, TokenType type) {
        this.value = new CharValue(value);
        this.type = type;
    }

    @Override
    public Value eval() {
        return value;
    }
}
