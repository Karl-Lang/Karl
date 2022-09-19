// Generated from java-escape by ANTLR 4.11.1
package fr.aiko.ryoko;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RyokoParser}.
 */
public interface RyokoListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RyokoParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(RyokoParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link RyokoParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(RyokoParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link RyokoParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(RyokoParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RyokoParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(RyokoParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RyokoParser#let}.
	 * @param ctx the parse tree
	 */
	void enterLet(RyokoParser.LetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RyokoParser#let}.
	 * @param ctx the parse tree
	 */
	void exitLet(RyokoParser.LetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RyokoParser#show}.
	 * @param ctx the parse tree
	 */
	void enterShow(RyokoParser.ShowContext ctx);
	/**
	 * Exit a parse tree produced by {@link RyokoParser#show}.
	 * @param ctx the parse tree
	 */
	void exitShow(RyokoParser.ShowContext ctx);
}