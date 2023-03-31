package me.aikoo.Karl.Interpreter.std;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public final class FunctionManager {
    private static File currentFile;
    private final static ArrayList<File> files = new ArrayList<>();

    public static void addFile(String name) {
        currentFile = new File(name);
        files.add(currentFile);
    }

    public static File getCurrentFile() {
        return currentFile;
    }

    public static Optional<File> getFile(String name) {
        return files.stream().filter(file -> Objects.equals(file.name, name)).findFirst(); // orElse error
    }

    public static void clear() {
        files.clear();
    }

    public static class File {
        private final HashMap<String, Function> functions = new HashMap<>();
        private final String name;

        public File(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addFunction(Function function) {
            functions.put(function.getName(), function);
        }

        public Function getFunction(String name) {
            return functions.get(name);
        }

        public boolean isFunction(String name) {
            return functions.containsKey(name);
        }

        public HashMap<String, Function> getFunctions() {
            return functions;
        }

        public void clear() {
            functions.clear();
        }

        public void removeFunction(String name) {
            functions.remove(name);
        }
    }
}
