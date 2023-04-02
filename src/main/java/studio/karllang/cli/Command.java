package studio.karllang.cli;

import java.util.ArrayList;

public abstract class Command {
    private final ArrayList<Options> allowedOptions = new ArrayList<>();
    private final String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract void run(ArrayList<Option> options) throws Exception;

    public String getName() {
        return name;
    }

    public ArrayList<Options> getAllowedOptions() {
        return allowedOptions;
    }
}
