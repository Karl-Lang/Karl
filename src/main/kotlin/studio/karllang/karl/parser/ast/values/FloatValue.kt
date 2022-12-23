package studio.karllang.karl.parser.ast.values

import studio.karllang.karl.lexer.TokenType

class FloatValue(private val value: Float) : Value() {
    override val type: TokenType = TokenType.FLOAT_VALUE

    override fun toString(): String {
        return java.lang.Float.toString(value)
    }

    override fun toInt(): Int {
        return toString().toInt()
    }

    override fun toFloat(): Float {
        return value
    }
}
