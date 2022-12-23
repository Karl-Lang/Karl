package studio.karllang.karl.parser.ast.values

import studio.karllang.karl.lexer.TokenType

class CharValue(val value: Char) : Value() {
    override val type: TokenType = TokenType.CHAR_VALUE

    override fun toString(): String {
        return value.toString()
    }

    override fun toInt(): Int {
        return toString().toInt()
    }

    override fun toFloat(): Float {
        return toString().toFloat()
    }
}