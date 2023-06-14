package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.statements.BlockStatement;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.File;

import java.util.LinkedHashMap;

public class FunctionExpression extends Expression {

    private final String name;
    private final LinkedHashMap<String, TokenType> args;
    private final BlockStatement body;
    private final TokenType type;
    private final File file;

    public FunctionExpression(String name, LinkedHashMap<String, TokenType> args, TokenType returnType, BlockStatement block, File file) {
        this.name = name;
        this.args = args;
        this.body = block;
        this.file = file;
        this.type = returnType;
    }

    @Override
    public Value eval() {
        return null;
    }
}
