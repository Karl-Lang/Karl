package studio.karllang.karl.errors.file

import studio.karllang.karl.Constants
import java.nio.file.Path
import kotlin.system.exitProcess

class FileNotFoundError(private val path: String) {
    init {
        printError()
    }

    private fun printError() {
        val fileName = Path.of(path).fileName.toString()
        System.err.println("${Constants.RED}-- FILE ERROR ------------------------------------------------ $fileName${Constants.RESET}")
        System.err.println(Constants.WHITE + "Description: " + "\u001B[0m" + Constants.RED + "The file must be a .karl file" + Constants.RESET)
        System.err.println("${Constants.WHITE}File path: ${Constants.RED}$path${Constants.RESET}")
        System.err.println("${Constants.RED}--------------------------------------------------------------------------${Constants.RESET}")
        exitProcess(0)
    }
}