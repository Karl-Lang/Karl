package studio.karllang.cli;

public enum Options {
    PATH(new String[]{"--path", "-p"}),
    DEBUG(new String[]{"--debug"});

    public final String[] name;

    Options(String[] name) {
        this.name = name;
    }
}