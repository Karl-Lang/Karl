package studio.karllang.karl.lib.std;

import studio.karllang.karl.lib.Function;
import studio.karllang.karl.lib.Library;

public class std_Show extends Function {
    public std_Show(Library std) {
        super("show", std);
    }

    @Override
    public void eval() {
        System.out.println("aa");
    }
}
