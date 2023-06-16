package studio.karllang.karl.lib;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.modules.File;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Set;

public final class LibraryManager {
    private static final ArrayList<Library> libraries = new ArrayList<>();
    private static final ArrayList<Library> importedLibrairies = new ArrayList<>();

    static {
        updateLibraries();
    }

    public static void importLibrary(String name, File file, int line, int pos) {
        if (!isLibrary(name)) {
            new RuntimeError("Unknown library: " + name, file.getStringPath(), line, pos);
            return;
        }

        Library library = getLibrary(name);
        importedLibrairies.add(library);
    }

    public static void addImportedLibrary(Library library) {
        importedLibrairies.add(library);
    }

    public static void updateLibraries() {
        ch.qos.logback.classic.Logger root;
        root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.reflections");
        root.setLevel(ch.qos.logback.classic.Level.OFF);

        Reflections reflections = new Reflections("studio.karllang.karl.lib");
        Set<Class<? extends Library>> classes = reflections.getSubTypesOf(Library.class);

        for (Class<? extends Library> c : classes) {
            try {
                if (Modifier.isAbstract(c.getModifiers()))
                    continue;

                Library library = c.getConstructor().newInstance();
                libraries.add(library);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Library getLibrary(String name) {
        return libraries.stream().filter(n -> n.getName().equals(name)).findFirst().orElse(null);
    }

    public static ArrayList<Library> getImportedLibrairies() {
        return importedLibrairies;
    }

    public static ArrayList<Library> getLibraries() {
        return libraries;
    }

    public static boolean isLibrary(String name) {
        return libraries.stream().anyMatch(n -> n.getName().equals(name));
    }
}
