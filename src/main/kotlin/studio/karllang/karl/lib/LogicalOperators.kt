package studio.karllang.karl.lib

import studio.karllang.karl.errors.runtime.RuntimeError
import studio.karllang.karl.lexer.TokenType
import studio.karllang.karl.parser.ast.values.Value
import java.util.*

class LogicalOperators {
    companion object {
        private val operators = HashMap<String, TokenType>()

        init {
            operators["&&"] = TokenType.AND
            operators["||"] = TokenType.OR
            operators["=="] = TokenType.EQUALEQUAL
            operators["!="] = TokenType.NOT_EQUAL
            operators[">"] = TokenType.GREATER
            operators["<"] = TokenType.LESS
            operators[">="] = TokenType.GREATER_EQUAL
            operators["<="] = TokenType.LESS_EQUAL
        }

        @JvmStatic
        fun and(a: Boolean, b: Boolean): Boolean {
            return a && b
        }

        @JvmStatic
        fun or(a: Boolean, b: Boolean): Boolean {
            return a || b
        }

        @JvmStatic
        fun isOperator(type: TokenType?): Boolean {
            return operators.containsValue(type)
        }

        @JvmStatic
        fun compare(firstNumber: Value, secondNumber: Value, operator: TokenType, line: Int, pos: Int): Boolean {
            if (!(listOf(TokenType.INT_VALUE, TokenType.FLOAT_VALUE).any { it == firstNumber.type }) && !(listOf(TokenType.INT_VALUE, TokenType.FLOAT_VALUE).any { it == secondNumber.type })) {
                throw RuntimeError("Type mismatch : " + firstNumber.type.toString().lowercase() + " and " + secondNumber.type.toString().lowercase(), pos, line, printString(firstNumber, secondNumber, operator))
            }
            return when (operator) {
                TokenType.LESS -> firstNumber.toFloat() < secondNumber.toFloat()
                TokenType.LESS_EQUAL -> firstNumber.toFloat() <= secondNumber.toFloat()
                TokenType.GREATER_EQUAL -> firstNumber.toFloat() >= secondNumber.toFloat()
                TokenType.GREATER -> firstNumber.toFloat() > secondNumber.toFloat()
                TokenType.EQUALEQUAL -> firstNumber.toFloat() == secondNumber.toFloat()
                TokenType.NOT_EQUAL -> firstNumber.toFloat() != secondNumber.toFloat()
                TokenType.OR, TokenType.AND -> true
                else -> throw RuntimeError("Unknown operator: $operator", pos, line, printString(firstNumber, secondNumber, operator))
            }
        }

        @JvmStatic
        fun not(parseBoolean: Boolean): Boolean {
            return !parseBoolean
        }

        @JvmStatic
        private fun printString(first: Value, second: Value, operator: TokenType): String {
            return first.toString() + " " + operator.value.lowercase(Locale.getDefault()) + " " + second.toString()
        }
    }
}