package studio.karllang.karl.parser.ast.statements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import studio.karllang.karl.errors.FileError.FileError;
import studio.karllang.karl.errors.FileError.FileNotFoundError;
import studio.karllang.karl.parser.Token;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;

public class UseStatement extends Statement {
    private final Expression expr;
    private final Path basePath;
    private final Token as;
    private final boolean lib;

    public UseStatement(Expression expr, Path basePath, Token as, boolean lib, int line, int pos) {
        super(line, pos);
        this.expr = expr;
        this.basePath = basePath;
        this.as = as;
        this.lib = lib;
    }

    @Override
    public void eval() {
        if (lib) evalLib();
        else evalAs();
    }

    private void evalAs() {
        System.out.println(this.as.getValue());
        Value value = expr.eval();
        File dir = new File(this.basePath.toUri());

        Optional<File> filePath = Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(f -> f.getName().equals(value.toString())).findFirst();

        if (filePath.isPresent()) {
            try {
                final File file = filePath.get();
                final String content = Files.readString(file.toPath());
            } catch (IOException e) {
                new FileError(dir.getAbsolutePath() + value);
            }
        } else {
            new FileNotFoundError(value.toString()); // TODO: Make better error message
        }
    }

    private void evalLib() {
        expr.eval();
    }
}
