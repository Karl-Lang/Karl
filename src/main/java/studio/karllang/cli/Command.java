package studio.karllang.cli;

import java.util.ArrayList;

/**
 * Represents a command in the Karl programming environment. A 'Command' object encapsulates the
 * name and description of a command, as well as the options that it accepts.
 */
public abstract class Command {
  private final ArrayList<Options> allowedOptions = new ArrayList<>();
  private final String name;
  private final String description;

  /**
   * Constructs a new 'Command' object with the specified name and description.
   *
   * @param name The name.
   * @param description The description.
   */
  public Command(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Runs the command with the specified options.
   *
   * @param options The options.
   * @throws Exception If an error occurs.
   */
  public abstract void run(ArrayList<Option> options) throws Exception;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public ArrayList<Options> getAllowedOptions() {
    return allowedOptions;
  }
}
