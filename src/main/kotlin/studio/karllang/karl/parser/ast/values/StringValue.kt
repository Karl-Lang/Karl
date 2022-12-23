package studio.karllang.karl.parser.ast.values

import studio.karllang.karl.lexer.TokenType

class StringValue(private val value: String) : Value() {
    override val type = TokenType.STR_VALUE

    override fun toString(): String {
        return value
    }

    override fun toInt(): Int {
        return value.toInt()
    }

    override fun toFloat(): Float {
        return value.toFloat()
    }
}