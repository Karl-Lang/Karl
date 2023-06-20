package studio.karllang.karl.lib.std.io;

import studio.karllang.karl.lib.Function;
import studio.karllang.karl.lib.Library;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;

public class Io extends Library {

    private final HashMap<String, Function> functions = new HashMap<>();

    public Io() {
        super("io");

        functions.put("Write", new io_Write(this));
    }

    @Override
    public HashMap<String, Function> getFunctions() {
        return functions;
    }

    public void run(String function, ArrayList<Expression> expressions) {
        if (!functions.containsKey(function)) {
            System.out.println("Unknown function: " + function);
            return;
        }
        functions.get(function).eval(expressions);
    }

    @Override
    public Function getFunction(String name) {
        return functions.get(name);
    }

    @Override
    public void loadSubLibrary(String name, File file, int line, int pos) {

    }
}
