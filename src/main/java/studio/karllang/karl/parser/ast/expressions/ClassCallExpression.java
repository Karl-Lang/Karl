package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.lib.Library;
import studio.karllang.karl.lib.LibraryManager;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.modules.File;

import java.util.ArrayList;

public class ClassCallExpression extends Expression {
    private final String name;
    private final File file;
    private ClassCallExpression child;
    private final int line;
    private final int pos;

    public ClassCallExpression(String name, File file, int line, int pos) {
        this.name = name;
        this.file = file;
        this.line = line;
        this.pos = pos;
    }

    public void addChild(ClassCallExpression child) {
        this.child = child;
    }

    public ClassCallExpression getChild() {
        return child;
    }

    public String getName() {
        return name;
    }

    @Override
    public Value eval() {
        if (child == null) {
            LibraryManager.importLibrary(name, file, line, pos);
        } else {
            Library library = LibraryManager.getLibrary(name);
            Library importedLib = library;
            ClassCallExpression childClass = child;
            while (childClass != null) {
                ClassCallExpression finalChildClass = childClass;
                Library subLib = library.getSubLibraries().stream().filter(n -> n.getName().equals(finalChildClass.getName())).findAny().orElse(null);
                if (subLib == null) {
                    new RuntimeError("Unknown library: " + childClass.getName(), file.getStringPath(), line, pos);
                    return null;
                }
                importedLib = subLib;
                childClass = childClass.getChild();
            }

            LibraryManager.addImportedLibrary(importedLib, file, line, pos);
        }

        return null;
    }
}
