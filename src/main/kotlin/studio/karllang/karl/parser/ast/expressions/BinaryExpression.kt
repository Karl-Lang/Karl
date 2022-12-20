package studio.karllang.karl.parser.ast.expressions

import studio.karllang.karl.olderrors.runtime.DivisionByZeroOldError
import studio.karllang.karl.olderrors.runtime.RuntimeOldError
import studio.karllang.karl.lexer.TokenType
import studio.karllang.karl.parser.ast.values.FloatValue
import studio.karllang.karl.parser.ast.values.IntValue
import studio.karllang.karl.parser.ast.values.Value
import studio.karllang.karl.parser.ast.values.StringValue
import studio.karllang.karl.std.Types

class BinaryExpression(
    private val left: Expression,
    private val right: Expression,
    private val operator: TokenType,
    private val fileName: String,
    private val line: Int,
    private val pos: Int
) : Expression() {

    override fun eval(): Value {
        val leftValue = left.eval()
        val rightValue = right.eval()

        return when {
            (leftValue.type == TokenType.INT_VALUE || leftValue.type == TokenType.FLOAT_VALUE) && (rightValue.type == TokenType.INT_VALUE || rightValue.type == TokenType.FLOAT_VALUE) -> when (operator) {
                TokenType.PLUS -> getIntOrFloatValue(leftValue.toFloat() + rightValue.toFloat())
                TokenType.MINUS -> getIntOrFloatValue(leftValue.toFloat() - rightValue.toFloat())
                TokenType.MULTIPLY -> getIntOrFloatValue(leftValue.toFloat() * rightValue.toFloat())
                TokenType.DIVIDE -> {
                    if (rightValue.toFloat() == 0f) {
                        DivisionByZeroOldError(fileName, line, pos)
                    }
                    getIntOrFloatValue(leftValue.toFloat() / rightValue.toFloat())
                }
                TokenType.MODULO -> getIntOrFloatValue(leftValue.toFloat() % rightValue.toFloat())
                else -> {
                    RuntimeOldError("Bad operator: ${operator.name}", fileName, line, pos)
                    null!!
                }
            }
            leftValue.type == TokenType.STR_VALUE || rightValue.type == TokenType.STR_VALUE -> {
                if (operator == TokenType.PLUS) {
                    StringValue(leftValue.toString() + rightValue.toString())
                } else {
                    RuntimeOldError("Bad operator: ${operator.name}", fileName, line, pos)
                    null!!
                }
            }
            else -> {
                RuntimeOldError("Unauthorized types for operation ${Types.getTypeName(leftValue.type)} and ${Types.getTypeName(rightValue.type)}", fileName, line, pos)
                null!!
            }
        }
    }

    private fun getIntOrFloatValue(result: Float): Value {
        return if (result % 1 == (0).toFloat()) IntValue(result.toInt()) else FloatValue(result)
    }
}
