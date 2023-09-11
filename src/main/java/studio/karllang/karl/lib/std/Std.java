package studio.karllang.karl.lib.std;

import java.util.ArrayList;
import java.util.HashMap;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.lib.Function;
import studio.karllang.karl.lib.Library;
import studio.karllang.karl.lib.std.io.Io;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.ast.expressions.Expression;

public class Std extends Library {

    private final HashMap<String, Function> functions = new HashMap<>();
    private final HashMap<String, Library> subLibraries = new HashMap<>();
    private final HashMap<String, Library> loadedSubLibraries = new HashMap<>();

    public Std() {
        super("std");

        subLibraries.put("io", new Io());
    }

    public void loadSubLibrary(String name, File file, int line, int pos) {
        if (!subLibraries.containsKey(name)) {
            new RuntimeError("Unknown sub library: " + name, file.getStringPath(), line, pos);
            return;
        }
        loadedSubLibraries.put(name, subLibraries.get(name));
    }

    @Override
    public Function getFunction(String name) {
        return functions.get(name);
    }

    public void run(String function, ArrayList<Expression> expressions) {
        if (!functions.containsKey(function)) {
            System.out.println("Unknown function: " + function);
            return;
        }
        functions.get(function).eval(expressions);
    }

    public ArrayList<Library> getSubLibraries() {
        return new ArrayList<>(subLibraries.values());
    }
}
