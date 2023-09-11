package studio.karllang.cli;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.LoggerFactory;

public class Main {
    private static final ArrayList<Option> options = new ArrayList<>();
    private static final CommandManager manager = new CommandManager();
    private static String command = "";

    public static void main(String[] args) throws Exception {
        ch.qos.logback.classic.Logger root;
        root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.reflections");
        root.setLevel(ch.qos.logback.classic.Level.OFF);

        if (args.length == 0) {
            printBaseText();
            return;
        }

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

        cmd.run(options);
    }

    private static void printBaseText() {
        System.out.println("KARL - PROGRAMMING LANGUAGE");
        System.out.println("Syntax: karl {command} [-options]");
        System.out.println("Commands:");

        manager.getCommands().forEach((name, command) -> {
            StringBuilder optionString = new StringBuilder().append("[");

            for (Options opt : command.getAllowedOptions()) {
                optionString.append(Arrays.toString(opt.name));
            }

            optionString.append("]");

            System.out.println("    " + name + " " + optionString + " - " + command.getDescription());
        });
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
