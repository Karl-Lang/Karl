package studio.karllang.karl.parser.ast.expressions

import studio.karllang.karl.errors.runtime.RuntimeError
import studio.karllang.karl.parser.ast.values.Value

abstract class Expression {
    @Throws(RuntimeError::class)
    abstract fun eval(): Value
}