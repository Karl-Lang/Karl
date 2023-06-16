package studio.karllang.karl.lib.std;

import studio.karllang.karl.lib.Function;
import studio.karllang.karl.lib.Library;

import java.util.ArrayList;
import java.util.HashMap;

public class Std extends Library {

    private final HashMap<String, Function> functions = new HashMap<>();

    public Std() {
        super("std");

        functions.put("show", new std_Show(this));
    }

    public void run(String function) {
        if (!functions.containsKey(function)) {
            System.out.println("Unknown function: " + function);
            return;
        }
        functions.get(function).eval();
    }
}
