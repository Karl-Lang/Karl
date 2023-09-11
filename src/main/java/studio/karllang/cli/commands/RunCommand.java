package studio.karllang.cli.commands;

import java.util.ArrayList;
import java.util.Optional;
import studio.karllang.cli.Command;
import studio.karllang.cli.Option;
import studio.karllang.cli.Options;
import studio.karllang.karl.Karl;

public class RunCommand extends Command {
    public RunCommand() {
        super("run", "Execute a Karl file");

        this.getAllowedOptions().add(Options.PATH);
        this.getAllowedOptions().add(Options.EXEC_TIME);
    }

    @Override
    public void run(ArrayList<Option> options) throws Exception {
        Optional<Option> path = options.stream().filter(opt -> opt.getType() == Options.PATH).findFirst();

        if (path.isPresent()) {
            options.remove(path.get());

            new Karl().run(path.get().getValue(), options);
        } else throw new Exception("No any path");
    }
}
