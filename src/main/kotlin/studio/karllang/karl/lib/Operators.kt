package studio.karllang.karl.lib

import studio.karllang.karl.lexer.TokenType

class Operators {
    companion object {
        private val operators = HashMap<String, TokenType>()

        init {
            operators["+"] = TokenType.PLUS
            operators["-"] = TokenType.MINUS
            operators["*"] = TokenType.MULTIPLY
            operators["/"] = TokenType.DIVIDE
            operators["%"] = TokenType.MODULO
        }

        @JvmStatic
        fun isOperator(type: TokenType): Boolean {
            return operators.containsValue(type)
        }
    }
}
