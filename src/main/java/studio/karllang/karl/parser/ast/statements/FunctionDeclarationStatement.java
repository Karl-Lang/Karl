package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.lib.LibraryManager;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.modules.ForbiddenNames;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.expressions.FunctionExpression;

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
        super(line, pos);
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
        checkName(name, file, line, pos);
        FunctionExpression expr = new FunctionExpression(name, args, type, body, file);
        expr.eval();
    }

    private void checkName(String name, File file, int line, int pos) {
        if (ForbiddenNames.isForbiddenName(name)) {
            new RuntimeError("Function name " + name + " is forbidden", file.getStringPath(), line, pos);
        } else if (file.getVariableManager().containsVariable(name)) {
            new RuntimeError("Function name " + name + " is already declared as a variable", file.getStringPath(), line, pos);
        } else if (LibraryManager.getImportedLibrairies().stream().anyMatch(n -> n.getName().equals(name))) {
            new RuntimeError("Cannot set a function name that is the same than a library: " + name, file.getStringPath(), line, pos);
        }
    }
}
