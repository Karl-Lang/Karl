package studio.karllang.cli;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager() {
        ch.qos.logback.classic.Logger root;
        root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.reflections");
        root.setLevel(ch.qos.logback.classic.Level.OFF);

        Reflections reflections = new Reflections("studio.karllang.cli.commands");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> c : classes) {
            try {
                Command command = c.getConstructor().newInstance();
                commands.put(command.getName(), command);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isCommand(String name) {
        return commands.containsKey(name);
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public HashMap<String, Command> getCommands() {
        return this.commands;
    }
}
