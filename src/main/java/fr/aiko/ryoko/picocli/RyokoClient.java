package fr.aiko.ryoko.picocli;

import fr.aiko.ryoko.Main;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "ryoko", mixinStandardHelpOptions = true, version = "ryoko 0.0.1",
        description = "Run Ryoko code")
public class RyokoClient implements Callable<String> {
    @CommandLine.Option(names = {"-f", "--file"}, description = "The Ryoko file to run")
    private String file;

    public static void main(String... args) throws Exception {
        int exitCode = new CommandLine(new RyokoClient()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public String call() throws Exception {
        if (file == null) {
            System.out.println("No file provided");
            return "No file provided";
        }

        Main.main(new String[]{file});

        return "Success";
    }
}
