package com.mengxinya.ys.checker.visitor;

import antlr4.parser.expr.CheckExprBaseVisitor;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.Evaluator;
import com.mengxinya.ys.checker.FunctionGetter;

public class JsonExprVisitor extends CheckExprBaseVisitor<Evaluator<JSONObject, JSONObject>> {
    private final CommonExprVisitor commonExprVisitor;

    public JsonExprVisitor(FunctionGetter functionGetter) {
        this.commonExprVisitor = VisitorFactories.CommonExprVisitorFactory.apply(functionGetter);
    }


    @Override
    public Evaluator<JSONObject, JSONObject> visitObjExpr(CheckExprParser.ObjExprContext ctx) {
        return commonExprVisitor.visitObjExpr(ctx);
    }

    @Override
    public Evaluator<JSONObject, JSONObject> visitOCommon(CheckExprParser.OCommonContext ctx) {
        Evaluator<JSONObject, ?> evaluator = commonExprVisitor.visit(ctx);
        return json -> (JSONObject) evaluator.eval(json);
    }

}
