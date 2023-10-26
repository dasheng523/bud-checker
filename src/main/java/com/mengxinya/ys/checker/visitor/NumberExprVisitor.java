package com.mengxinya.ys.checker.visitor;

import antlr4.parser.expr.CheckExprBaseVisitor;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.Evaluator;
import com.mengxinya.ys.checker.FunctionGetter;

public class NumberExprVisitor extends CheckExprBaseVisitor<Evaluator<JSONObject, Double>> {

    private final CommonExprVisitor commonExprVisitor;

    public NumberExprVisitor(FunctionGetter functionGetter) {
        this.commonExprVisitor = VisitorFactories.CommonExprVisitorFactory.apply(functionGetter);
    }

    @Override
    public Evaluator<JSONObject, Double> visitNCommon(CheckExprParser.NCommonContext ctx) {
        return null;
    }

    @Override
    public Evaluator<JSONObject, Double> visitNum(CheckExprParser.NumContext ctx) {
        return commonExprVisitor.visitNum(ctx);
    }


    @Override
    public Evaluator<JSONObject, Double> visitOp2(CheckExprParser.Op2Context ctx) {
        return commonExprVisitor.visitOp2(ctx);
    }

    @Override
    public Evaluator<JSONObject, Double> visitOp1(CheckExprParser.Op1Context ctx) {
        return commonExprVisitor.visitOp1(ctx);
    }
}
