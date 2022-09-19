package fr.aiko.ryoko;

import java.util.HashMap;

public class RyokoCustomListener extends RyokoBaseListener {
    HashMap<String, Integer> variableMap = new HashMap<>();

    @Override
    public void exitShow(RyokoParser.ShowContext ctx) {
        if(ctx.INT() != null){
            System.out.println(ctx.INT().getText());
        }
        else if(ctx.VAR() != null){
            System.out.println(this.variableMap.get(ctx.VAR().getText()));
        }
    }

    // Handle let statement
    @Override
    public void exitLet(RyokoParser.LetContext ctx) {
        this.variableMap.put(ctx.VAR().getText(),
                Integer.parseInt(ctx.INT().getText()));
    }
}
