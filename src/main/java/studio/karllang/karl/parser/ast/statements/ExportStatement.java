package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.Token;

public class ExportStatement extends Statement {

    public final Token token;

    public ExportStatement(Token token) {
        this.token = token;
    }

    @Override
    public void eval() {
        System.out.println("Exported identifier: " + this.token.getValue());
    }
}
