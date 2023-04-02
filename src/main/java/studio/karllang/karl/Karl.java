package studio.karllang.karl;

import studio.karllang.cli.Option;
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

public class Karl {

    public void run(String path, ArrayList<Option> options) {
        String isEnabled = "false";
        if (!Files.exists(Path.of(path))) {
            new FileNotFoundError(path);
        }
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!fileName.endsWith(".karl")) {
            new FileError(path);
        }

        try {
            VariableManager.addFile(fileName);
            FunctionManager.addFile(fileName);

            ArrayList<Token> tokens = new Lexer(Files.readString(Path.of(path)), path).tokens;
            ArrayList<Statement> statements = new Parser(tokens, path).parse();

            Long start = System.currentTimeMillis();
            statements.forEach(Statement::eval);
            Long end = System.currentTimeMillis();

            VariableManager.clear();
            FunctionManager.clear();

            if (Boolean.parseBoolean(isEnabled)) {
                long elapsedTime = end - start;
                System.out.println("Execution time: " + elapsedTime + "ms");
            }
        } catch (IOException e) {
            new FileError(path);
        }

        /*try {
            String[] array = new String[100000 * 100000];
        } catch (OutOfMemoryError e) {
            System.out.println("Out of memory uwu baka");
        }*/
    }
}