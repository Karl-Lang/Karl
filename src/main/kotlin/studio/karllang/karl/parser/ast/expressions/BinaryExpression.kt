package studio.karllang.karl.parser.ast.expressions

import studio.karllang.karl.errors.runtime.DivisionByZeroError
import studio.karllang.karl.errors.runtime.RuntimeError
import studio.karllang.karl.lexer.TokenType
import studio.karllang.karl.parser.ast.values.FloatValue
import studio.karllang.karl.parser.ast.values.IntValue
import studio.karllang.karl.parser.ast.values.StringValue
import studio.karllang.karl.parser.ast.values.Value
import studio.karllang.karl.std.Types

class BinaryExpression(
    private val left: Expression,
    private val right: Expression,
    private val operator: TokenType,
    private val line: Int,
    private val pos: Int
) : Expression() {

    override fun eval(): Value {
        val leftValue = left.eval()
        val rightValue = right.eval()

        return when {
            leftValue.type in listOf(
                TokenType.INT_VALUE,
                TokenType.FLOAT_VALUE
            ) && rightValue.type in listOf(TokenType.INT_VALUE, TokenType.FLOAT_VALUE) -> when (operator) {
                TokenType.PLUS -> getIntOrFloatValue(leftValue.toFloat() + rightValue.toFloat())
                TokenType.MINUS -> getIntOrFloatValue(leftValue.toFloat() - rightValue.toFloat())
                TokenType.MULTIPLY -> getIntOrFloatValue(leftValue.toFloat() * rightValue.toFloat())
                TokenType.DIVIDE -> {
                    if (rightValue.toFloat() == 0f) throw DivisionByZeroError(pos, line, toString())
                    getIntOrFloatValue(leftValue.toFloat() / rightValue.toFloat())
                }

                TokenType.MODULO -> getIntOrFloatValue(leftValue.toFloat() % rightValue.toFloat())
                else -> throw RuntimeError("Bad operator: ${operator.value}", pos, line, toString())
            }

            listOf(leftValue.type, rightValue.type).any { it == TokenType.STR_VALUE } -> {
                if (operator == TokenType.PLUS) {
                    StringValue(leftValue.toString() + rightValue.toString())
                } else throw RuntimeError("Bad operator: ${operator.value}", pos, line, toString())
            }

            else -> throw RuntimeError(
                "Unauthorized types for operation ${Types.getTypeName(leftValue.type)} and ${
                    Types.getTypeName(
                        rightValue.type
                    )
                }", pos, line, toString()
            )
        }
    }

    private fun getIntOrFloatValue(result: Float): Value {
        return if (result % 1 == 0f) IntValue(result.toInt()) else FloatValue(result)
    }

    override fun toString(): String {
        return left.eval().toString() + " " + operator.value + " " + right.eval().toString()
    }
}
