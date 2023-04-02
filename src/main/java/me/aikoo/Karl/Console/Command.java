package me.aikoo.Karl.Console;

import me.aikoo.Karl.Console.Option;
import me.aikoo.Karl.Console.Options;

import java.util.ArrayList;

public abstract class Command {
    final ArrayList<Options> allowedOptions = new ArrayList<>();
    final String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract void run(ArrayList<Option> options);

    public String getName() {
        return name;
    }

    public ArrayList<Options> getAllowedOptions() {
        return allowedOptions;
    }
}
