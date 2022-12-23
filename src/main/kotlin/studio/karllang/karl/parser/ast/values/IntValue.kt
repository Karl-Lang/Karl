package studio.karllang.karl.parser.ast.values

import studio.karllang.karl.lexer.TokenType

class IntValue(private val value: Int) : Value() {
    override val type: TokenType = TokenType.INT_VALUE

    override fun toString(): String {
        return value.toString()
    }

    override fun toInt(): Int {
        return value
    }

    override fun toFloat(): Float {
        return toString().toFloat()
    }
}