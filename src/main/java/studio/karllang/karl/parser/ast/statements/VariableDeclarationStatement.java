package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.lexer.Token;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.expressions.ValueExpression;
import studio.karllang.karl.parser.ast.expressions.VariableExpression;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.lib.ForbiddenNames;
import studio.karllang.karl.lib.Types;
import studio.karllang.karl.lib.VariableManager;

public class VariableDeclarationStatement extends Statement {
    private final String name;
    private final Token type;
    private final int line;
    private final int pos;
    private final boolean isFinal;
    private Expression expression;

    public VariableDeclarationStatement(Expression expression, String name, Token type, int line, int pos, boolean isFinal) {
        this.expression = expression;
        this.name = name;
        this.type = type;
        this.line = line;
        this.pos = pos;
        this.isFinal = isFinal;
    }

    @Override
    public void eval() throws RuntimeError {
        if (ForbiddenNames.isForbiddenName(name)) {
            throw new RuntimeError("Variable name " + name + " is forbidden", pos, line, printString());
        }

        if (VariableManager.getVariable(name) != null) {
            throw new RuntimeError("Variable " + name + " is already declared", pos, line, printString());
        }

        Value value = expression.eval();
        if (type.getType() == TokenType.FLOAT && value.getType() == TokenType.INT_VALUE) {
            expression = new ValueExpression(value.toFloat(), TokenType.FLOAT_VALUE);
            value = expression.eval();
        }

        if (value.toString().equals("null_void")) {
            throw new RuntimeError("Cannot assign void function to a variable", pos, line, printString());
        }

        if (!Types.checkValueType(type.getType(), value.getType()) && value.getType() != TokenType.NULL) {
            throw new RuntimeError("Expected type " + Types.getTypeName(type.getType()) + " but got " + Types.getTypeName(value.getType()), pos - 1, line, printString());
        }

        if (value.getType() == TokenType.NULL && type.getType() != TokenType.STRING && type.getType() != TokenType.CHAR) {
            throw new RuntimeError(Types.getTypeName(type.getType()) + " variable cannot be null", pos - 1, line, printString());
        }

        VariableExpression expr = new VariableExpression(name, value, isFinal);
        expr.setValue(value);
        expr.eval();
    }

    private String printString() throws RuntimeError {
        return type.getValue() + ":" + name + " = " + expression.eval().toString();
    }
}
