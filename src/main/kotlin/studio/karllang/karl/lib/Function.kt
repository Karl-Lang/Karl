package studio.karllang.karl.lib

import studio.karllang.karl.errors.runtime.RuntimeError
import studio.karllang.karl.lexer.TokenType
import studio.karllang.karl.parser.ast.expressions.Expression
import studio.karllang.karl.parser.ast.statements.BlockStatement
import studio.karllang.karl.parser.ast.values.Value

class Function(
    private val name: String,
    private val args: LinkedHashMap<String, TokenType>,
    private val type: TokenType,
    private val body: BlockStatement
) {
    fun eval(values: ArrayList<Expression>, line: Int, pos: Int): Value? {
        val arguments = HashMap<String, Value>()
        args.keys.zip(values).forEach { (k, v) -> arguments[k] = v.eval() }
        body.setArgs(arguments)
        body.eval()

        val result = body.result
        if (result != null) {
            if (type == TokenType.VOID) {
                throw RuntimeError("Function $name is void, but return a value", pos, line, printString())
            }
            if (Types.checkValueType(type, result.type) || (type == TokenType.STRING && result.type == TokenType.NULL)) {
                return result
            } else {
                throw RuntimeError("Incorrect return type for function $name: except ${type.name} but got type ${result.type.name}", pos, line, printString())
            }
        } else if (type != TokenType.VOID) {
            throw RuntimeError("Missing return statement in function: $name", pos, line, printString())
        }

        return null
    }

    fun getName(): String {
        return name
    }

    fun getArgs(): LinkedHashMap<String, TokenType> {
        return args
    }

    fun getType(): TokenType {
        return type
    }

    private fun printString(): String {
        val optionString = StringBuilder()
        for (key in args.keys) {
            optionString.append(if (optionString.isEmpty()) key else "$key, ")
        }
        return "$name($optionString);"
    }
}