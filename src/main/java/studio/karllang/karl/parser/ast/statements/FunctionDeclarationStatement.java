package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.expressions.FunctionExpression;
import studio.karllang.karl.modules.File;

import java.util.LinkedHashMap;

public class FunctionDeclarationStatement extends Statement {
    private final String name;
    private final LinkedHashMap<String, TokenType> args;
    private final BlockStatement body;
    private final TokenType type;
    private final File file;
    private final String fileName;
    private final int line;
    private final int pos;

    public FunctionDeclarationStatement(String name, LinkedHashMap<String, TokenType> args, TokenType returnType, BlockStatement block, File file, int line, int pos) {
        this.name = name;
        this.args = args;
        this.body = block;
        this.type = returnType;
        this.file = file;
        this.fileName = file.getName();
        this.line = line;
        this.pos = pos;
    }

    @Override
    public void eval() {
        FunctionExpression expr = new FunctionExpression(name, args, type, body, file);
        expr.eval();
    }
}
