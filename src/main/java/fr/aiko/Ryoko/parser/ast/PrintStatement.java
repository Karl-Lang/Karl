package fr.aiko.Ryoko.parser.ast;

public class PrintStatement extends Statement {
    private final String expr;

    public PrintStatement(String expr) {
        this.expr = expr;
    }

    @Override
    public void execute() {
        System.out.println(this.expr);
    }
}
