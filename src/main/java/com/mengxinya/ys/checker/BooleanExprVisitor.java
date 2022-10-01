package com.mengxinya.ys.checker;

import antlr4.parser.expr.CheckExprBaseVisitor;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;

public class BooleanExprVisitor extends CheckExprBaseVisitor<Evaluator<JSONObject, Boolean>> {
    private final NumberExprVisitor numberExprVisitor = new NumberExprVisitor();

    @Override
    public Evaluator<JSONObject, Boolean> visitBLiteral(CheckExprParser.BLiteralContext ctx) {
        String text = ctx.getText();
        boolean rs = text.equals("true");
        return json -> rs;
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitOpExpr(CheckExprParser.OpExprContext ctx) {
        String compareOp = ctx.compareOp().getText();
        Evaluator<JSONObject, Double> evaluator0 = numberExprVisitor.visit(ctx.numberExpr(0));
        Evaluator<JSONObject, Double> evaluator1 = numberExprVisitor.visit(ctx.numberExpr(1));

        return jsonObject -> {
            Double num0 = evaluator0.eval(jsonObject);
            Double num1 = evaluator1.eval(jsonObject);

            return switch (compareOp) {
                case ">" -> num0.compareTo(num1) > 0;
                case ">=" -> num0.compareTo(num1) >= 0;
                case "<" -> num0.compareTo(num1) < 0;
                case "<=" -> num0.compareTo(num1) <= 0;
                case "==" -> num0.compareTo(num1) == 0;
                case "!=" -> num0.compareTo(num1) != 0;
                default -> throw new RuntimeException();
            };
        };
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitBHolder(CheckExprParser.BHolderContext ctx) {
        String text = ctx.placeholder().getText();
        String field = ctx.placeholder().getText().substring(2, text.length() - 1);
        return jsonObject -> {
            Object value = CheckerUtils.getJsonValue(jsonObject, field);
            return Boolean.parseBoolean(value.toString());
        };
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitBFunc(CheckExprParser.BFuncContext ctx) {
        return super.visitBFunc(ctx);
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitNot(CheckExprParser.NotContext ctx) {
        Evaluator<JSONObject, Boolean> evaluator = visit(ctx.booleanExpr());
        return jsonObject -> !evaluator.eval(jsonObject);
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitOr(CheckExprParser.OrContext ctx) {
        Evaluator<JSONObject, Boolean> evaluator0 = visit(ctx.booleanExpr(0));
        Evaluator<JSONObject, Boolean> evaluator1 = visit(ctx.booleanExpr(1));
        return jsonObject -> {
            boolean rs1 = evaluator0.eval(jsonObject);
            if (rs1) {
                return true;
            }
            return evaluator1.eval(jsonObject);
        };
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitAnd(CheckExprParser.AndContext ctx) {
        Evaluator<JSONObject, Boolean> evaluator0 = visit(ctx.booleanExpr(0));
        Evaluator<JSONObject, Boolean> evaluator1 = visit(ctx.booleanExpr(1));
        return jsonObject -> {
            boolean rs1 = evaluator0.eval(jsonObject);
            if (!rs1) {
                return false;
            }
            return evaluator1.eval(jsonObject);
        };
    }

    @Override
    public Evaluator<JSONObject, Boolean> visitBBrackets(CheckExprParser.BBracketsContext ctx) {
        return visit(ctx.booleanExpr());
    }
}
