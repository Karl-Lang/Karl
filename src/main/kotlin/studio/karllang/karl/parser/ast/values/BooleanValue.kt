package studio.karllang.karl.parser.ast.values

import studio.karllang.karl.lexer.TokenType

class BooleanValue(private val value: Boolean) : Value() {
    override val type: TokenType = TokenType.BOOL_VALUE

    override fun toString(): String {
        return value.toString()
    }

    override fun toInt(): Int {
        return 0
    }

    override fun toFloat(): Float {
        return 0f
    }
}