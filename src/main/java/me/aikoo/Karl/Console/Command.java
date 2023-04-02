package me.aikoo.Karl.Console;

import me.aikoo.Karl.Console.Option;
import me.aikoo.Karl.Console.Options;

import java.util.ArrayList;

public abstract class Command {
    final ArrayList<Option> options;
    final ArrayList<Options> allowedOptions = new ArrayList<>();
    final String name;

    public Command(String name, ArrayList<Option> options) {
        this.name = name;
        this.options = options;
    }

    public abstract void run();

    public ArrayList<Option> getOptions() {
        return options;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Options> getAllowedOptions() {
        return allowedOptions;
    }
}
