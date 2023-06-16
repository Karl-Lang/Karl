package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.lib.Library;
import studio.karllang.karl.lib.LibraryManager;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.NullValue;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.modules.Function;
import studio.karllang.karl.modules.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FuncCallExpression extends Expression {
    private final String name;
    private final ArrayList<Expression> args;
    private final String fileName;
    private final File file;
    private final int line;
    private final int pos;
    private boolean isFound = false;

    public FuncCallExpression(String name, ArrayList<Expression> args, File file, int line, int pos) {
        this.name = name;
        this.args = args;
        this.file = file;
        this.fileName = file.getName();
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() {
        if (this.file.getFunctionManager().isFunction(name)) {
            Function function = this.file.getFunctionManager().getFunction(name);
            LinkedHashMap<String, TokenType> parameters = function.getArgs();
            if (args.size() != parameters.size()) {
                new RuntimeError("Function " + name + " takes " + parameters.size() + " arguments, " + args.size() + " given", file.getStringPath(), line, pos);
            }

            int i = 0;
            for (String arg : parameters.keySet()) {
                if (!Types.checkValueType(parameters.get(arg), args.get(i).eval().getType())) {
                    new RuntimeError("Type mismatch for argument " + arg + " of function " + name + ": Excepted type " + Types.getTypeName(parameters.get(arg)) + ", but got type " + Types.getTypeName(args.get(i).eval().getType()), file.getStringPath(), line, pos);
                }
                i++;
            }

            if (function.getType() == TokenType.VOID) {
                function.eval(args, file, line, pos);
                return new NullValue("null_void");
            } else return function.eval(args, file, line, pos);
        } else {
            ArrayList<Library> libraries = LibraryManager.getImportedLibrairies();
            for (Library library : libraries) {
                if (library.getFunctions().containsKey(name)) {
                    studio.karllang.karl.lib.Function function = library.getFunctions().get(name);
                    isFound = true;
                    function.eval(args);
                } else if (library.getLoadedSubLibraries().size() > 0) {
                    System.out.println("called");
                    library.getLoadedSubLibraries().forEach((name, subLibrary) -> {
                        if (subLibrary.getFunctions().containsKey(name)) {
                            studio.karllang.karl.lib.Function function = subLibrary.getFunctions().get(name);
                            isFound = true;
                            function.eval(args);
                        }
                    });
                }
            }

            if (!isFound) new RuntimeError("Unknown function: " + name, file.getStringPath(), line, pos);
            return null;
        }
    }
}
