package com.mengxinya.ys.parser.visitor;

import antlr4.parser.expr.CheckExprBaseVisitor;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.common.Evaluator;
import com.mengxinya.ys.funcgetter.FunctionGetter;

public class StringExprVisitor extends CheckExprBaseVisitor<Evaluator<JSONObject, String>> {
    private final CommonExprVisitor commonExprVisitor;

    public StringExprVisitor(FunctionGetter functionGetter) {
        this.commonExprVisitor = VisitorFactories.CommonExprVisitorFactory.apply(functionGetter);
    }

    @Override
    public Evaluator<JSONObject, String> visitStr(CheckExprParser.StrContext ctx) {
        return commonExprVisitor.visitStr(ctx);
    }

    @Override
    public Evaluator<JSONObject, String> visitSCommon(CheckExprParser.SCommonContext ctx) {
        Evaluator<JSONObject, ?> evaluator = commonExprVisitor.visit(ctx);
        return json -> (String) evaluator.eval(json);
    }
}
