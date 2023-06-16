package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.File;

import java.util.ArrayList;
import java.util.Optional;

public class ClassCallExpression extends Expression {
    private final String name;
    private final File file;
    private final ArrayList<ClassCallExpression> childs = new ArrayList<>();
    private final int line;
    private final int pos;

    public ClassCallExpression(String name, File file, int line, int pos) {
        this.name = name;
        this.file = file;
        this.line = line;
        this.pos = pos;
    }

    public void addChild(ClassCallExpression child) {
        childs.add(child);
    }

    public String getName() {
        return name;
    }

    @Override
    public Value eval() {
        return null;
    }
}
