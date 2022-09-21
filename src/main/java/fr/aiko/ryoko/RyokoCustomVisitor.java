package fr.aiko.ryoko;

import fr.aiko.RyokoBaseVisitor;
import fr.aiko.RyokoParser;
import fr.aiko.ryoko.exceptions.NotDeclaredVariableException;
import fr.aiko.ryoko.exceptions.RyukoException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RyokoCustomVisitor extends RyokoBaseVisitor {

    private final Map<String, Object> variableMap = new HashMap<>();
    private final Map<String, RyokoParser.FuncDeclarationContext> functions = new HashMap<>();
    private final Map<String, Map<String, Object>> functionVariableMap = new HashMap<>();
    @Override
    public Object visitSystemLib(RyokoParser.SystemLibContext ctx) {
        var mapUsed = ctx.getParent().getParent().getParent() instanceof RyokoParser.FuncDeclarationContext ? functionVariableMap.get(((RyokoParser.FuncDeclarationContext) ctx.getParent().getParent().getParent()).IDENTIFIER().getText()) : variableMap;
        if (ctx.SHOW() != null) {
            if (ctx.exprList() != null) {
                StringBuilder str = new StringBuilder();
                for (RyokoParser.ExprContext expr : ctx.exprList().expr()) {
                    if (expr.IDENTIFIER() != null) {
                        Object variable = mapUsed.get(expr.IDENTIFIER().getText());
                        if (variable == null) {
                            throw new NotDeclaredVariableException(expr.IDENTIFIER().getText());
                        }
                        String result = variable.toString().replaceAll("\"", "");
                        str.append(result);
                    } else {
                        str.append(expr.getText().replaceAll("\"", ""));
                    }
                }
                System.out.println(str);
            } else {
                throw new RyukoException("No argument provided to show function");
            }
        }
    return null;
}

@Override
public Object visitVarDeclaration(RyokoParser.VarDeclarationContext ctx) {
    var mapUsed = ctx.getParent().getParent().getParent() instanceof RyokoParser.FuncDeclarationContext ? functionVariableMap.get(((RyokoParser.FuncDeclarationContext) ctx.getParent().getParent().getParent()).IDENTIFIER().getText()) : variableMap;
    String varName = ctx.IDENTIFIER().getText();
    if (ctx.expr().IDENTIFIER() != null) {
        Object variable = mapUsed.get(ctx.expr().IDENTIFIER().getText());
        if (variable == null) {
            throw new NotDeclaredVariableException(ctx.expr().IDENTIFIER().getText());
        }
        mapUsed.put(varName, variable);
    } else {
        mapUsed.put(varName, ctx.expr().getText());
    }
    return null;
}

    // Visit function declaration
    @Override
    public Object visitFuncDeclaration(RyokoParser.FuncDeclarationContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        functions.put(funcName, ctx);
        functionVariableMap.put(funcName, new HashMap<>());
        // TODO: Check if the function is already declared
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

        // Stock all the arguments in a list
        if (func.parameter() != null) {
            if (ctx.exprList() != null) {
                List<Object> args = new ArrayList<>();

                for (RyokoParser.ExprContext expr : ctx.exprList().expr()) {
                    if (expr.IDENTIFIER() != null) {
                        Object variable = variableMap.get(expr.IDENTIFIER().getText());
                        if (variableMap.isEmpty() || variable == null) {
                            throw new NotDeclaredVariableException(expr.IDENTIFIER().getText());
                        }
                        args.add(variable);
                    } else {
                        args.add(expr.getText().replaceAll("\"", ""));
                    }
                }

                // Stock all the arguments in the function variable map
                for (int i = 0; i < func.parameter().IDENTIFIER().size(); i++) {
                    functionVariableMap.get(funcName).put(func.parameter().IDENTIFIER(i).getText(), args.get(i));
                }

                visit(func.funcBody());
            }
        }
        return null;
    }
}
