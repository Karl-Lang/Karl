package studio.karllang.karl.parser.ast.values

import studio.karllang.karl.lexer.TokenType

class NullValue(val value: String) : Value() {
    override val type: TokenType = TokenType.NULL

    override fun toString(): String {
        return value
    }

    override fun toInt(): Int {
        return 0
    }

    override fun toFloat(): Float {
        return 0f
    }
}