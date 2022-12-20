package studio.karllang.karl.errors

import studio.karllang.karl.Constants
import java.nio.file.Path

class LexicalError(
    private val type: String,
    private val msg: String,
    private val line: Int,
    private val toStr: String
) : Throwable() {
    private lateinit var path: String

    fun printError() {
        println("${Constants.RED}-- ${type.uppercase()} ------------------------------------------------ ${getFileName()}${Constants.RESET}")
        println(Constants.WHITE + "Description: " + "\u001B[0m" + Constants.RED + msg + Constants.RESET)
        println("${Constants.WHITE}File path: ${Constants.RED}$path${Constants.RESET}\n")
        println(Constants.RED + line + " | " + toStr + Constants.RESET)
        println("${Constants.RED}--------------------------------------------------------------------------${Constants.RESET}")
    }

    fun setPath(path: String): LexicalError {
        this.path = path
        return this
    }

    private fun getFileName(): String {
        return try {
            Path.of(path).fileName.toString()
        } catch (e: Exception) {
            path.split("\\", "/")[path.split("\\", "/").size]
        }
    }
}