package studio.karllang.karl;

import studio.karllang.cli.Option;
import studio.karllang.cli.Options;
import studio.karllang.karl.errors.FileError.FileError;
import studio.karllang.karl.errors.FileError.FileNotFoundError;
import studio.karllang.karl.parser.Lexer;
import studio.karllang.karl.parser.Parser;
import studio.karllang.karl.parser.Token;
import studio.karllang.karl.parser.ast.statements.Statement;
import studio.karllang.karl.std.FunctionManager;
import studio.karllang.karl.std.VariableManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

public class Karl {

    public void run(String pathStr, ArrayList<Option> options) {
        if (options == null) options = new ArrayList<>();
        Optional<Option> isEnabled = options.stream().filter(opt -> opt.getType() == Options.EXEC_TIME).findFirst();
        final Path path = Path.of(pathStr);
        if (!Files.exists(path)) {
            new FileNotFoundError(pathStr);
        }
        String fileName = pathStr.substring(pathStr.lastIndexOf("/") + 1);

        if (!fileName.endsWith(".karl")) {
            new FileError(pathStr);
        }

        try {
            VariableManager.addFile(fileName);
            FunctionManager.addFile(fileName);

            ArrayList<Token> tokens = new Lexer(Files.readString(path), pathStr).tokens;
            ArrayList<Statement> statements = new Parser(tokens, pathStr).parse();

            Long start = System.currentTimeMillis();
            statements.forEach(Statement::eval);
            Long end = System.currentTimeMillis();

            VariableManager.clear();
            FunctionManager.clear();

            if (isEnabled.isPresent() && (Boolean.parseBoolean(isEnabled.get().getValue()) || isEnabled.get().getValue() == null)) {
                long elapsedTime = end - start;
                System.out.println("Execution time: " + elapsedTime + "ms");
            }
        } catch (IOException e) {
            new FileError(pathStr);
        }

        /*try {
            String[] array = new String[100000 * 100000];
        } catch (OutOfMemoryError e) {
            System.out.println("Out of memory uwu baka");
        }*/
    }
}