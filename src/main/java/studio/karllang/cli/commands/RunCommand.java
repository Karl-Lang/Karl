package studio.karllang.cli.commands;

import studio.karllang.cli.Command;
import studio.karllang.cli.Option;
import studio.karllang.cli.Options;

import java.util.ArrayList;

public class RunCommand extends Command {
    public RunCommand() {
        super("run");

        this.getAllowedOptions().add(Options.PATH);
    }

    @Override
    public void run(ArrayList<Option> options) {
        System.out.println("Heyyyyy");
    }
}
