package studio.karllang.cli;

public enum Options {
    PATH(new String[]{"--path", "-p"}),
    DEBUG(new String[]{"--debug"}),
    EXEC_TIME(new String[]{"--exec-time", "-et"});

    public final String[] name;

    Options(String[] name) {
        this.name = name;
    }
}
