package studio.karllang.karl.lexer

class Token(private val type: TokenType, private var value: String, private val position: Int, private val line: Int) {
    fun getType(): TokenType {
        return type
    }

    fun getValue(): String {
        return value
    }

    fun setValue(newValue: String) {
        value = newValue
    }

    fun getPosition(): Int {
        return position
    }

    fun getLine(): Int {
        return line
    }
}