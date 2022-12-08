package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.parser.ast.expressions.ValueExpression;
import fr.aiko.Karl.parser.ast.expressions.VariableExpression;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.Types;

public class VariableDeclarationStatement extends Statement {
    private Expression expression;
    private final String name;
    private final TokenType type;
    private final String fileName;
    private final int line;
    private final int pos;

    public VariableDeclarationStatement(Expression expression, String name, TokenType type, String fileName, int line, int pos) {
        this.expression = expression;
        this.name = name;
        this.type = type;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public void eval() {
        Value value = expression.eval();
        if (type == TokenType.FLOAT && value.getType() == TokenType.INT_VALUE) {
            expression = new ValueExpression(value.toFloat(), TokenType.FLOAT_VALUE);
            value = expression.eval();
        }

        if (value.toString().equals("null_void")) {
            new RuntimeError("Cannot assign void function to a variable", fileName, line, pos);
        }

        if (!Types.checkValueType(type, value.getType()) && value.getType() != TokenType.NULL) {
            new RuntimeError("Expected type " + type.getName() + " but got " + value.getType().toString().toLowerCase(), fileName, line, pos - 1);
        }

        if (value.getType() == TokenType.NULL && type != TokenType.STRING && type != TokenType.CHAR) {
            new RuntimeError(type + " variable cannot be null", fileName, line, pos - 1);
        }

        VariableExpression expr = new VariableExpression(name, value);
        expr.setValue(value);

        expr.eval();
    }
}
