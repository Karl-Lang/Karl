package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.runtime.RuntimeError;

public abstract class Statement {
    public abstract void eval() throws RuntimeError;
}
