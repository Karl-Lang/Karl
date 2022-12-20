package studio.karllang.karl.errors

import studio.karllang.karl.Constants
import studio.karllang.karl.lexer.Lexer
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
    private var lexer: Lexer? = null

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
            val str = Lexer(toStr).tokens
            val indicator = StringBuilder()
            for (i in 0 until pos) {
                indicator.append(" ".repeat(str[i].getValue().length))
            }
            indicator.append("^")
            return indicator.toString()
        } catch (e: LexicalError) {
            e.setPath(path).printError()
            exitProcess(1)
        }
    }
}