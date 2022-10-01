package com.mengxinya.ys.checker;

import antlr4.parser.expr.CheckExprBaseVisitor;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;

public class NumberExprVisitor extends CheckExprBaseVisitor<Evaluator<JSONObject, Double>> {
    @Override
    public Evaluator<JSONObject, Double> visitNBrackets(CheckExprParser.NBracketsContext ctx) {
        return visit(ctx.numberExpr());
    }

    @Override
    public Evaluator<JSONObject, Double> visitNHolder(CheckExprParser.NHolderContext ctx) {
        return jsonObject -> {
            String text = ctx.placeholder().getText();
            String field = ctx.placeholder().getText().substring(2, text.length() - 1);
            Object value = CheckerUtils.getJsonValue(jsonObject, field);
            return Double.parseDouble(value.toString());
        };
    }

    @Override
    public Evaluator<JSONObject, Double> visitNum(CheckExprParser.NumContext ctx) {
        double text = Double.parseDouble(ctx.getText());
        return jsonObject -> text;
    }

    @Override
    public Evaluator<JSONObject, Double> visitNFunc(CheckExprParser.NFuncContext ctx) {
        return super.visitNFunc(ctx);
    }

    @Override
    public Evaluator<JSONObject, Double> visitOp2(CheckExprParser.Op2Context ctx) {
        String op = ctx.numberOp2().getText();
        Evaluator<JSONObject, Double> evaluator0 = visit(ctx.numberExpr(0));
        Evaluator<JSONObject, Double> evaluator1 = visit(ctx.numberExpr(1));
        return jsonObject -> {
            double num0 = evaluator0.eval(jsonObject);
            double num1 = evaluator1.eval(jsonObject);
            if (op.equals("+")) {
                return num0 + num1;
            } else {
                return num0 - num1;
            }
        };
    }

    @Override
    public Evaluator<JSONObject, Double> visitOp1(CheckExprParser.Op1Context ctx) {
        String op = ctx.numberOp1().getText();
        Evaluator<JSONObject, Double> evaluator0 = visit(ctx.numberExpr(0));
        Evaluator<JSONObject, Double> evaluator1 = visit(ctx.numberExpr(1));
        return jsonObject -> {
            double num0 = evaluator0.eval(jsonObject);
            double num1 = evaluator1.eval(jsonObject);
            if (op.equals("*")) {
                return num0 * num1;
            } else {
                return num0 / num1;
            }
        };
    }
}
