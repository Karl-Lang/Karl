package studio.karllang.karl.lib;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Library {
    private final String name;
    private final ArrayList<Library> subLibraries = new ArrayList<>();
    private final HashMap<String, Function> functions = new HashMap<>();

    public Library(String name) {
        this.name = name;
    }

    public abstract void run(String function);

    public void addSubLibrary(Library library) {
        subLibraries.add(library);
    }

    public ArrayList<Library> getSubLibraries() {
        return subLibraries;
    }

    public String getName() {
        return name;
    }
}
