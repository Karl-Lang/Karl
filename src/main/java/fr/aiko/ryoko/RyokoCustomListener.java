package fr.aiko.ryoko;

import fr.aiko.RyokoBaseListener;
import fr.aiko.RyokoParser;
import fr.aiko.ryoko.exceptions.IncorrectTypeException;
import fr.aiko.ryoko.exceptions.IncorrectValueTypeException;
import fr.aiko.ryoko.exceptions.NotDeclaredVariableException;
import fr.aiko.ryoko.exceptions.RyukoException;

import java.util.HashMap;
import java.util.Map;

public class RyokoCustomListener extends RyokoBaseListener {
    Map<String, Object> variableMap = new HashMap<>();

    @Override
    public void exitVarDeclaration(RyokoParser.VarDeclarationContext ctx) {
        String type = ctx.TYPE().getText();
        if (!type.equals("string") && !type.equals("int") && !type.equals("bool")) {
            throw new RyukoException("Incorrect variable type : Variable type must be 'string', 'bool' or 'int'.");
        }

        String varName = ctx.IDENTIFIER().getText();

        switch (type) {
            case "int" -> {
                if (ctx.expr().INTEGER() == null) {
                    throw new IncorrectValueTypeException(varName, ctx.expr().getText(), "integer");
                }

                variableMap.put(varName, ctx.expr().INTEGER());
            }
            case "string" -> {
                if (ctx.expr().STRING() == null) {
                    throw new IncorrectValueTypeException(varName, ctx.expr().getText(), "string");
                }

                variableMap.put(varName, ctx.expr().STRING().getText().substring( 1, ctx.expr().STRING().getText().length() - 1 ));
            }
            default -> {
                throw new IncorrectTypeException(type);
            }
        }
    }

    @Override
    public void exitSystemLib(RyokoParser.SystemLibContext ctx) {
        if (ctx.SHOW() != null) { // Si c'est un print
            if (ctx.expr().IDENTIFIER() != null) {
                Object variable = variableMap.get(ctx.expr().IDENTIFIER().getText());
                if (variableMap.isEmpty() || variable == null) {
                    throw new NotDeclaredVariableException(ctx.expr().IDENTIFIER().getText());
                }
                System.out.println(variable);
            } else {
                String str = ctx.expr().getText();
                System.out.println(str.substring( 1, str.length() - 1 ));
            }
        }
    }
}
