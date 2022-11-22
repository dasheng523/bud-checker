package com.mengxinya.ys.logiclang;

import antlr4.parser.expr.LogiclangBaseVisitor;
import antlr4.parser.expr.LogiclangParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class LogicExprVisitorImpl extends LogiclangBaseVisitor<LogicExpr> {
    @Override
    public LogicExpr visitDistinctExpr(LogiclangParser.DistinctExprContext ctx) {
        return LangTests.makeDistinctConditionExpr(ctx.VARNAME().stream().map(ParseTree::getText).toList());
    }

    @Override
    public LogicExpr visitStmt(LogiclangParser.StmtContext ctx) {
        List<LogiclangParser.ExprContext> exprContextList = ctx.expr();
        LogicExpr[] logicExprArr = new LogicExpr[exprContextList.size()];
        for (int i = 0; i < exprContextList.size(); i++) {
            logicExprArr[i] = visit(exprContextList.get(i));
        }
        return LangTests.composeExpr(logicExprArr);
    }

    @Override
    public LogicExpr visitMaybeExpr(LogiclangParser.MaybeExprContext ctx) {
        String varName = ctx.VARNAME().getText();
        List<Integer> integerList = ctx.INT().stream().map(item -> Integer.parseInt(item.getText())).toList();
        return LangTests.makeMaybeExpr(varName, integerList);
    }

    @Override
    public LogicExpr visitNotEqExpr(LogiclangParser.NotEqExprContext ctx) {
        Integer num = Integer.parseInt(ctx.INT().getText());
        String varName = ctx.VARNAME().getText();
        return LangTests.makeNotConditionExpr(varName, num);
    }

    @Override
    public LogicExpr visitGtExpr(LogiclangParser.GtExprContext ctx) {
        String varName0 = ctx.VARNAME(0).getText();
        String varName1 = ctx.VARNAME(1).getText();
        return LangTests.makeGtConditionExpr(varName0, varName1);
    }

    @Override
    public LogicExpr visitNotNearExpr(LogiclangParser.NotNearExprContext ctx) {
        String varName0 = ctx.VARNAME(0).getText();
        String varName1 = ctx.VARNAME(1).getText();
        return LangTests.makeNotNearConditionExpr(varName0, varName1);
    }

    @Override
    public LogicExpr visitPrintExpr(LogiclangParser.PrintExprContext ctx) {
        List<TerminalNode> nodeList = ctx.VARNAME();
        String[] varNames = new String[nodeList.size()];
        for (int i = 0; i < nodeList.size(); i++) {
            varNames[i] = nodeList.get(i).getText();
        }
        return LangTests.makePrintExpr(varNames);
    }
}
