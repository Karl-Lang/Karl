package studio.karllang.karl.lib;

import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Library {
    private final String name;
    private final ArrayList<Library> subLibraries = new ArrayList<>();
    private final HashMap<String, Function> functions = new HashMap<>();
    private final HashMap<String, Library> loadedSubLibraries = new HashMap<>();

    public Library(String name) {
        this.name = name;
    }

    public HashMap<String, Function> getFunctions() {
        return functions;
    }

    public abstract void run(String function, ArrayList<Expression> expressions);

    public abstract void loadSubLibrary(String name, File file, int line, int pos);

    public void addSubLibrary(Library library) {
        subLibraries.add(library);
    }

    public abstract Function getFunction(String name);

    public HashMap<String, Library> getLoadedSubLibraries() {
        return loadedSubLibraries;
    }

    public ArrayList<Library> getSubLibraries() {
        return subLibraries;
    }

    public String getName() {
        return name;
    }
}
