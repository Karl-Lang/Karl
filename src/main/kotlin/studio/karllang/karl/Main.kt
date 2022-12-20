package studio.karllang.karl

import picocli.CommandLine
import studio.karllang.karl.errors.Error
import studio.karllang.karl.errors.LexicalError
import studio.karllang.karl.errors.file.FileError
import studio.karllang.karl.errors.file.FileNotFoundError
import studio.karllang.karl.errors.syntax.SyntaxError
import studio.karllang.karl.lexer.Lexer
import studio.karllang.karl.parser.Parser
import studio.karllang.karl.std.FunctionManager
import studio.karllang.karl.std.VariableManager
import java.nio.file.Files
import java.nio.file.Path
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    CommandLine(Main()).execute(*args)
}

@CommandLine.Command(
    name = "karl",
    mixinStandardHelpOptions = true,
    version = [Constants.VERSION],
    description = ["Karl programming language"]
)
class Main : Runnable {
    @CommandLine.Parameters(index = "0", description = ["The Karl file to run"])
    lateinit var path: String

    override fun run() {
        try {
            val file = Path.of(path)
            if (!Files.exists(file) || !Files.isRegularFile(file) || !Files.isReadable(file)) {
                FileNotFoundError(path)
                return
            } else if (!path.endsWith(".karl")) {
                FileError(path)
                return
            }

            val lexer = Lexer(Files.readString(file))
            val parser = Parser(lexer.tokens, path)
            val startTime = System.currentTimeMillis()
            val ast = parser.parse()
            ast.forEach { it.eval() }
            val endTime = System.currentTimeMillis()

            FunctionManager.clear()
            VariableManager.clear()

            println("Execution time: ${endTime - startTime}ms")
        } catch (e: LexicalError) {
            e.setPath(path).printError()
            exitProcess(1)
        } catch (e: SyntaxError) {
            Error("Syntax Error", e.getMsg(), path, e.getPosition(), e.getLine(), e.getLineString()).printError()
            exitProcess(1)
        }
    }
}