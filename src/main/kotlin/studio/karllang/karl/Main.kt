package studio.karllang.karl

import picocli.CommandLine
import studio.karllang.karl.errors.FileError.FileError
import studio.karllang.karl.errors.FileError.FileNotFoundError
import studio.karllang.karl.parser.Lexer
import studio.karllang.karl.parser.Parser
import studio.karllang.karl.std.FunctionManager
import studio.karllang.karl.std.VariableManager
import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    CommandLine(Main()).execute(*args)
}

@CommandLine.Command(
    name = "karl",
    mixinStandardHelpOptions = true,
    version = ["karl 0.4.0-alpha.5"],
    description = ["karl programming language"]
)
class Main : Runnable {
    @CommandLine.Parameters(index = "0", description = ["The file to run"])
    lateinit var path: String

    override fun run() {
        if (!Files.exists(Path.of(path)) || !Files.isRegularFile(Path.of(path)) || !Files.isReadable(Path.of(path))) {
            FileNotFoundError(path)
            return
        } else if (!path.endsWith(".karl")) {
            FileError(path)
            return
        }

        val name = Path.of(path).fileName.toString()
        val lexer = Lexer(Files.readString(Path.of(path)), name)
        val parser = Parser(lexer.tokens, name)
        val startTime = System.currentTimeMillis()
        parser.parse().forEach { it.eval() }
        val endTime = System.currentTimeMillis()

        FunctionManager.clear()
        VariableManager.clear()

        println("Execution time: ${endTime - startTime}ms")
    }
}