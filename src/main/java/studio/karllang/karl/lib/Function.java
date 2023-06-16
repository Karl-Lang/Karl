package studio.karllang.karl.lib;

public abstract class Function {
    private final String name;
    private final Library library;

    public Function(String name, Library library) {
        this.name = name;
        this.library = library;
    }

    public abstract void eval();
}
