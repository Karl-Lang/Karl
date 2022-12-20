package studio.karllang.karl.errors

import studio.karllang.karl.Constants
import studio.karllang.karl.lexer.Lexer
import studio.karllang.karl.lexer.TokenType
import java.nio.file.Path
import kotlin.system.exitProcess

class Error(
    private val type: String,
    private val msg: String,
    private val path: String,
    private val pos: Int,
    private val line: Int,
    private val toStr: String
) {
    private var fileName: String = Path.of(path).fileName.toString()

    fun printError() {
        val indicator = printIndicator()
        System.err.println("${Constants.RED}-- ${type.uppercase()} ------------------------------------------------ $fileName${Constants.RESET}")
        System.err.println(Constants.WHITE + "Description: " + "\u001B[0m" + Constants.RED + msg + Constants.RESET)
        System.err.println("${Constants.WHITE}File path: ${Constants.RED}$path${Constants.RESET}\n")
        System.err.println(Constants.RED + line + " | " + toStr + Constants.RESET)
        System.err.println(Constants.RED + "   $indicator" + Constants.RESET)
        System.err.println("${Constants.RED}--------------------------------------------------------------------------${Constants.RESET}")
    }

    private fun printIndicator(): String {
        try {
            val lexer = Lexer(toStr)
            val str = lexer.tokens
            val indicator = StringBuilder()
            for (i in 0 until getNewPos(lexer, pos, line) - 1) {
                if (i < str.size && str[i].getType() != TokenType.EOF) {
                    indicator.append(" ".repeat(str[i].getValue().length))
                }
            }
            indicator.append("^")
            return indicator.toString()
        } catch (e: LexicalError) {
            e.setPath(path).printError()
            exitProcess(1)
        }
    }

    private fun getNewPos(lexer: Lexer, pos: Int, line: Int): Int {
        for (tkn in lexer.tokens) {
            if (tkn.getLine() == line) {
                return pos - tkn.getPosition()
            }
        }
        return pos
    }
}