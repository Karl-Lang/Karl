package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.parser.ast.values.Value;

public abstract class Expression {
    public abstract Value eval();
}
