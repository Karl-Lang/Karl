package studio.karllang.karl;

import org.slf4j.LoggerFactory;
import studio.karllang.cli.Option;
import studio.karllang.cli.Options;
import studio.karllang.karl.errors.FileError.FileError;
import studio.karllang.karl.errors.FileError.FileNotFoundError;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.lib.LibraryManager;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.Lexer;
import studio.karllang.karl.parser.Parser;
import studio.karllang.karl.parser.Token;
import studio.karllang.karl.parser.ast.statements.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

public class Karl {

    public void run(String pathStr, ArrayList<Option> options) {
        if (options == null) options = new ArrayList<>();

        Path path = Path.of(pathStr);
        String fileName = pathStr.substring(pathStr.lastIndexOf("/") + 1);
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        Optional<Option> isEnabled = options.stream().filter(opt -> opt.getType() == Options.EXEC_TIME).findFirst();

        if (!Files.exists(path)) {
            new FileNotFoundError(pathStr);
        }

        if (!fileExtension.equals("karl")) {
            new FileError(pathStr);
        }

        File file = new File(fileName, fileExtension, pathStr);

        try {
            ArrayList<Token> tokens = new Lexer(Files.readString(path), file).tokens;
            ArrayList<Statement> statements = new Parser(tokens, file).parse();

            Long start = System.currentTimeMillis();
            Statement currentStatement = null;
            try {
                for (Statement statement : statements) {
                    currentStatement = statement;
                    statement.eval();
                }
            } catch (StackOverflowError e) {
                new RuntimeError("Fatal: Stack Overflow", file.getStringPath(), currentStatement.getLine(), currentStatement.getPos());
            }

            Long end = System.currentTimeMillis();
            file.getFunctionManager().clear();
            file.getVariableManager().clear();
            LibraryManager.clearImportedLibraries();

            if (isEnabled.isPresent() && (Boolean.parseBoolean(isEnabled.get().getValue()) || isEnabled.get().getValue() == null)) {
                long elapsedTime = end - start;
                System.out.println("Execution time: " + elapsedTime + "ms");
            }
        } catch (IOException e) {
            new FileError(pathStr);
        }
    }
}