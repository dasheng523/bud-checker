package com.mengxinya.ys.checker.visitor;

import antlr4.parser.expr.CheckExprBaseVisitor;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.Evaluator;
import com.mengxinya.ys.checker.FunctionGetter;

public class BooleanExprVisitor extends CheckExprBaseVisitor<Evaluator<JSONObject, Boolean>> {
    private final CommonExprVisitor commonExprVisitor;

    public BooleanExprVisitor(FunctionGetter functionGetter) {
        this.commonExprVisitor = VisitorFactories.CommonExprVisitorFactory.apply(functionGetter);
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitBLiteral(CheckExprParser.BLiteralContext ctx) {
        return commonExprVisitor.visitBLiteral(ctx);
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitOpExpr(CheckExprParser.OpExprContext ctx) {
        return commonExprVisitor.visitOpExpr(ctx);
    }


    @Override
    public Evaluator<JSONObject, Boolean> visitNot(CheckExprParser.NotContext ctx) {
        return commonExprVisitor.visitNot(ctx);
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitOr(CheckExprParser.OrContext ctx) {
        return commonExprVisitor.visitOr(ctx);
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitAnd(CheckExprParser.AndContext ctx) {
        return commonExprVisitor.visitAnd(ctx);
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitBCommon(CheckExprParser.BCommonContext ctx) {
        return null;
    }
}
