package studio.karllang.cli.commands;

import studio.karllang.cli.Command;
import studio.karllang.cli.Option;

import java.util.ArrayList;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("version");
    }

    @Override
    public void run(ArrayList<Option> options) {
        System.out.println("Karl CLI v0.0.1");
    }
}
