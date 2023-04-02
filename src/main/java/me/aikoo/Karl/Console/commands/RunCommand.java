package me.aikoo.Karl.Console.commands;

import me.aikoo.Karl.Console.Command;
import me.aikoo.Karl.Console.Option;
import me.aikoo.Karl.Console.Options;

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
