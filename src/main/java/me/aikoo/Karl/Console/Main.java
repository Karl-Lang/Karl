package me.aikoo.Karl.Console;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static final ArrayList<Option> options = new ArrayList<>();
    private static final CommandManager manager = new CommandManager();
    private static String command = "";

    public static void main(String[] args) throws Exception {
        Main.lex(args);

        if (!manager.isCommand(command)) {
            throw new Exception("Unknown command: " + command);
        }

        Command cmd = manager.getCommand(command);
        for (Option opt : options) {
            if (!cmd.getAllowedOptions().contains(opt.getType())) {
                throw new Exception("Unexpected option for " + cmd.getName() + " command: " + opt.getType().name().toLowerCase());
            }
        }
    }

    private static void lex(String[] arguments) throws Exception {
        int index = 0;

        if (arguments[index].startsWith("-")) throw new Exception("Can't start with an option");
        Main.command = arguments[index];

        ArrayList<String> args = new ArrayList<>(Arrays.asList(arguments));
        args.remove(0);

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (index + 1 < args.size() && !args.get(index + 1).startsWith("-")) {
                    options.add(new Option(args.get(index + 1), arg));
                    index += 2;
                } else {
                    options.add(new Option(null, arg));
                    index++;
                }
            }
        }
    }
}
