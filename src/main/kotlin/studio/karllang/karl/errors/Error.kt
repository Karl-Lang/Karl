package studio.karllang.karl.errors

import studio.karllang.karl.Constants
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.system.exitProcess

open class Error(
    private val name: String,
    private val message: String,
    private val path: String,
    private val line: Int,
    private var position: Int
) {
    init {
        position = getPosition(position)
        printError()
    }

    private fun printError() {
        val fileName = Path.of(path).fileName.toString()

        System.err.println("${Constants.RED}-- ${name.uppercase(Locale.getDefault())} ------------------------------------------------ $fileName${Constants.RESET}")
        System.err.println(Constants.WHITE + "Description: " + "\u001B[0m" + Constants.RED + message + Constants.RESET)
        System.err.println("${Constants.WHITE}File path: ${Constants.RED}$path${Constants.RESET}\n")
        System.err.println(Constants.RED + line + " | " + getLine() + Constants.RESET)
        System.err.println(Constants.RED + "   " + printIndicator() + Constants.RESET)
        System.err.println("${Constants.RED}--------------------------------------------------------------------------${Constants.RESET}")

        exitProcess(0)
    }

    private fun getLine(): String? {
        try {
            return Files.readAllLines(Path.of(path))[line - 1]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun printIndicator(): String {
        val line = getLine()
        return if (line != null) {
            val array = line.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val indicator = StringBuilder()
            for (i in array.indices) {
                indicator.append(if (i == position) "^" else " ")
            }

            if (!indicator.toString().endsWith("^") && !indicator.toString().contains("^")) {
                indicator.append("^")
            }
            indicator.toString()
        } else {
            "^"
        }
    }

    private fun getPosition(givePos: Int): Int {
        return try {
            val lines = Files.readAllLines(Path.of(path)).subList(0, line - 1).toTypedArray()
            val pos = Arrays.stream(lines).mapToInt { obj: String -> obj.length }.sum()
            givePos - pos
        } catch (ignored: IOException) {
            givePos
        }
    }
}