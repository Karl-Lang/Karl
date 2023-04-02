package studio.karllang.cli;

import java.util.ArrayList;

public abstract class Command {
    private final ArrayList<Options> allowedOptions = new ArrayList<>();
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void run(ArrayList<Option> options) throws Exception;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Options> getAllowedOptions() {
        return allowedOptions;
    }
}
