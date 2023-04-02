package me.aikoo.Karl.Console;

import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager() {
        Reflections reflections = new Reflections("me.aikoo.Karl.Console.commands");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> c : classes) {
            try {
                Command command = c.getConstructor(ArrayList.class).newInstance(new ArrayList<>());
                commands.put(command.getName(), command);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
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
}
