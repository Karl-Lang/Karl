package fr.aiko.ryoko;

import fr.aiko.RyokoBaseVisitor;
import fr.aiko.RyokoParser;
import fr.aiko.ryoko.exceptions.NotDeclaredVariableException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RyokoCustomVisitor extends RyokoBaseVisitor {

    private final Map<String, Object> variableMap = new HashMap<>();
    private final Map<String, RyokoParser.FuncDeclarationContext> functions = new HashMap<>();
    @Override
    public Object visitSystemLib(RyokoParser.SystemLibContext ctx) {
        if (ctx.SHOW() != null) {
            if (ctx.expr().IDENTIFIER() != null) {
                Object variable = variableMap.get(ctx.expr().IDENTIFIER().getText());
                if (variableMap.isEmpty() || variable == null) {
                    throw new NotDeclaredVariableException(ctx.expr().IDENTIFIER().getText());
                }
                System.out.println(variable);
            } else {
                String str = ctx.expr().getText();
                System.out.println(str.replaceAll("\"", ""));
            }
        }
        return null;
    }

    @Override
    public Object visitVarDeclaration(RyokoParser.VarDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        if (ctx.expr().IDENTIFIER() != null) {
            Object variable = variableMap.get(ctx.expr().IDENTIFIER().getText());
            if (variableMap.isEmpty() || variable == null) {
                throw new NotDeclaredVariableException(ctx.expr().IDENTIFIER().getText());
            }
            variableMap.put(varName, variable);
        } else {
            variableMap.put(varName, ctx.expr().getText());
        }
        return null;
    }

    // Visit function declaration
    @Override
    public Object visitFuncDeclaration(RyokoParser.FuncDeclarationContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        functions.put(funcName, ctx);
        return null;
    }

    // Visit function call
    @Override
    public Object visitFunctionCall(RyokoParser.FunctionCallContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        RyokoParser.FuncDeclarationContext func = functions.get(funcName);
        if (func == null) {
            throw new NotDeclaredVariableException(funcName);
        }

        visit(func.funcBody());
        return null;
    }
}
