package studio.karllang.cli.commands;

import studio.karllang.Constants;
import studio.karllang.cli.Command;
import studio.karllang.cli.Option;

import java.util.ArrayList;

public class VersionCommand extends Command {

  public VersionCommand() {
    super("version", "Show Karl's versions");
  }

  @Override
  public void run(ArrayList<Option> options) {
    System.out.println("Karl CLI " + Constants.CLI_VERSION);
    System.out.println("Karl " + Constants.KARL_VERSION);
  }
}
