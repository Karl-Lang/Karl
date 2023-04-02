package studio.karllang.cli.commands;

import studio.karllang.cli.Command;
import studio.karllang.cli.Option;
import studio.karllang.cli.Options;
import studio.karllang.karl.Karl;

import java.util.ArrayList;
import java.util.Optional;

public class RunCommand extends Command {
    public RunCommand() {
        super("run");

        this.getAllowedOptions().add(Options.PATH);
    }

    @Override
    public void run(ArrayList<Option> options) throws Exception {
        Optional<Option> path = options.stream().filter(opt -> opt.getType() == Options.PATH).findFirst();

        System.out.println("UwU " + path.isPresent());
        if (path.isPresent()) {
            options.remove(path.get());
            System.out.println("Running Karl...");

            new Karl().run(path.get().getValue(), options);
        } else throw new Exception("No any path");
    }
}
