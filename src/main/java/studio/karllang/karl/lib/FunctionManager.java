package studio.karllang.karl.lib;

import java.util.HashMap;

public final class FunctionManager {
    private static final HashMap<String, Function> functions = new HashMap<>();

    public static void addFunction(Function function) {
        functions.put(function.getName(), function);
    }

    public static Function getFunction(String name) {
        return functions.get(name);
    }

    public static boolean isFunction(String name) {
        return functions.containsKey(name);
    }

    public static void clear() {
        functions.clear();
    }

}
