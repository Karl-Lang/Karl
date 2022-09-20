package fr.aiko.ryoko;

import fr.aiko.RyokoBaseListener;
import fr.aiko.RyokoParser;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;

public class RyokoCustomListener extends RyokoBaseListener {
    HashMap<String, Pair<Object, Pair<String, Boolean>>> variableMap = new HashMap<>();


    @Override
    public void exitVarDeclaration(RyokoParser.VarDeclarationContext ctx) {
        // this.variableMap.put(ctx.VAR().getText(), Integer.parseInt(ctx.INT().getText()));
        String type = ctx.TYPE().getText();
        if (!type.equals("string") && !type.equals("int") && !type.equals("bool")) {
            System.out.println("Incorrect type declaration");
        }

        String varName = ctx.IDENTIFIER().getText();
        String value = ctx.expr().getText();

        variableMap.put(varName, new Pair<>(value, new Pair(type, false)));
    }

    /*@Override
    public void exitPrint(RyokoParser.PrintContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            System.out.println(ctx.IDENTIFIER());
        } else if (ctx.STRING() != null) {
            System.out.println(ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1));
        }
    }*/

    @Override
    public void exitSystemLib(RyokoParser.SystemLibContext ctx) {
        if (ctx.SHOW() != null) {
            System.out.println(ctx.expr().getText().substring(1, ctx.expr().getText().length() - 1));
        }
    }
}
