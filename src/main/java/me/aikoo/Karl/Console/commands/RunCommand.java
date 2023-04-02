package me.aikoo.Karl.Console.commands;

import me.aikoo.Karl.Console.Command;
import me.aikoo.Karl.Console.Option;
import me.aikoo.Karl.Console.Options;

import java.util.ArrayList;

public class RunCommand extends Command {
    public RunCommand(ArrayList<Option> options) {
        super("run", options);

        this.getAllowedOptions().add(Options.PATH);
    }

    @Override
    public void run() {
        System.out.println("Heyyyyy");
    }
}
