package studio.karllang.karl.parser.ast.values

import studio.karllang.karl.lexer.TokenType

abstract class Value {
    abstract val type: TokenType

    abstract override fun toString(): String
    abstract fun toInt(): Int
    abstract fun toFloat(): Float
}