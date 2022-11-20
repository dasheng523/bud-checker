//package com.mengxinya.ys.checker.visitor;
//
//import antlr4.parser.expr.CheckExprBaseVisitor;
//import antlr4.parser.expr.CheckExprParser;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.mengxinya.ys.checker.*;
//
//import java.util.List;
//
//public class CommonExprToSqlQueryVisitor extends CheckExprBaseVisitor<Evaluator<JSONObject, SqlCondition>> {
//    private final FunctionGetter functionGetter;
//
//    public CommonExprToSqlQueryVisitor(FunctionGetter functionGetter) {
//        this.functionGetter = functionGetter;
//    }
//
//
//    @Override
//    public Evaluator<JSONObject, SqlCondition> visitCommonHolder(CheckExprParser.CommonHolderContext ctx) {
//        String text = ctx.placeholder().getText();
//        String field = ctx.placeholder().getText().substring(2, text.length() - 1);
//        return jsonObject -> {
//            Object val = CheckerUtils.getJsonValue(jsonObject, field);
//        };
//    }
//
//    @Override
//    public Evaluator<JSONObject, ?> visitFunction(CheckExprParser.FunctionContext ctx) {
//        List<CheckExprParser.ExprContext> list = ctx.expr();
//        String funcName = ctx.funcName().getText();
//
//        Evaluator<List<?>, ?> evaluator = functionGetter.getFunction(funcName);
//        if (evaluator == null) {
//            throw new ExprSyntaxException("The function is not support: " + funcName);
//        }
//
//        return jsonObject -> {
//            List<?> params = list.stream()
//                    .map(this::visit)
//                    .map(item -> item.eval(jsonObject))
//                    .toList();
//            return evaluator.eval(params);
//        };
//    }
//
//    @Override
//    public Evaluator<JSONObject, ?> visitCommonBracket(CheckExprParser.CommonBracketContext ctx) {
//        CheckExprParser.ExprContext expr = ctx.expr();
//        return visit(expr);
//    }
//
//
//    @Override
//    public Evaluator<JSONObject, Boolean> visitBLiteral(CheckExprParser.BLiteralContext ctx) {
//        String text = ctx.getText();
//        boolean rs = text.equals("true");
//        return json -> rs;
//    }
//
//    @Override
//    public Evaluator<JSONObject, Boolean> visitOpExpr(CheckExprParser.OpExprContext ctx) {
//        String compareOp = ctx.compareOp().getText();
//        Evaluator<JSONObject, ?> evaluator0 = visit(ctx.numberExpr(0));
//        Evaluator<JSONObject, ?> evaluator1 = visit(ctx.numberExpr(1));
//
//        return jsonObject -> {
//            Double num0 = (Double)evaluator0.eval(jsonObject);
//            Double num1 = (Double)evaluator1.eval(jsonObject);
//
//            return switch (compareOp) {
//                case ">" -> num0.compareTo(num1) > 0;
//                case ">=" -> num0.compareTo(num1) >= 0;
//                case "<" -> num0.compareTo(num1) < 0;
//                case "<=" -> num0.compareTo(num1) <= 0;
//                case "==" -> num0.compareTo(num1) == 0;
//                case "!=" -> num0.compareTo(num1) != 0;
//                default -> throw new RuntimeException();
//            };
//        };
//    }
//
//
//    @Override
//    public Evaluator<JSONObject, Boolean> visitNot(CheckExprParser.NotContext ctx) {
//        Evaluator<JSONObject, ?> evaluator = visit(ctx.booleanExpr());
//        return jsonObject -> !(Boolean)evaluator.eval(jsonObject);
//    }
//
//    @Override
//    public Evaluator<JSONObject, Boolean> visitOr(CheckExprParser.OrContext ctx) {
//        Evaluator<JSONObject, ?> evaluator0 = visit(ctx.booleanExpr(0));
//        Evaluator<JSONObject, ?> evaluator1 = visit(ctx.booleanExpr(1));
//        return jsonObject -> {
//            boolean rs1 = (Boolean) evaluator0.eval(jsonObject);
//            if (rs1) {
//                return true;
//            }
//            return (Boolean) evaluator1.eval(jsonObject);
//        };
//    }
//
//    @Override
//    public Evaluator<JSONObject, Boolean> visitAnd(CheckExprParser.AndContext ctx) {
//        Evaluator<JSONObject, ?> evaluator0 = visit(ctx.booleanExpr(0));
//        Evaluator<JSONObject, ?> evaluator1 = visit(ctx.booleanExpr(1));
//        return jsonObject -> {
//            boolean rs1 = (Boolean)evaluator0.eval(jsonObject);
//            if (!rs1) {
//                return false;
//            }
//            return (Boolean)evaluator1.eval(jsonObject);
//        };
//    }
//
//    @Override
//    public Evaluator<JSONObject, Boolean> visitBCommon(CheckExprParser.BCommonContext ctx) {
//        Evaluator<JSONObject, ?> evaluator = visit(ctx.commonExpr());
//        return json -> (Boolean) evaluator.eval(json);
//    }
//
//
//    /***** numberExpr ***/
//
//    @Override
//    public Evaluator<JSONObject, Double> visitNCommon(CheckExprParser.NCommonContext ctx) {
//        Evaluator<JSONObject, ?> evaluator = visit(ctx.commonExpr());
//        return json -> Double.parseDouble(evaluator.eval(json).toString());
//    }
//
//    @Override
//    public Evaluator<JSONObject, Double> visitNum(CheckExprParser.NumContext ctx) {
//        double text = Double.parseDouble(ctx.number().getText());
//        return jsonObject -> text;
//    }
//
//
//    @Override
//    public Evaluator<JSONObject, Double> visitOp2(CheckExprParser.Op2Context ctx) {
//        String op = ctx.numberOp2().getText();
//        Evaluator<JSONObject, ?> evaluator0 = visit(ctx.numberExpr(0));
//        Evaluator<JSONObject, ?> evaluator1 = visit(ctx.numberExpr(1));
//        return jsonObject -> {
//            double num0 = (Double)evaluator0.eval(jsonObject);
//            double num1 = (Double)evaluator1.eval(jsonObject);
//            if (op.equals("+")) {
//                return num0 + num1;
//            } else {
//                return num0 - num1;
//            }
//        };
//    }
//
//    @Override
//    public Evaluator<JSONObject, Double> visitOp1(CheckExprParser.Op1Context ctx) {
//        String op = ctx.numberOp1().getText();
//        Evaluator<JSONObject, ?> evaluator0 = visit(ctx.numberExpr(0));
//        Evaluator<JSONObject, ?> evaluator1 = visit(ctx.numberExpr(1));
//        return jsonObject -> {
//            double num0 = (Double)evaluator0.eval(jsonObject);
//            double num1 = (Double)evaluator1.eval(jsonObject);
//            if (op.equals("*")) {
//                return num0 * num1;
//            } else {
//                return num0 / num1;
//            }
//        };
//    }
//
//
//
//    /********* JSON **********/
//
//    @Override
//    public Evaluator<JSONObject, JSONObject> visitObjExpr(CheckExprParser.ObjExprContext ctx) {
//        String objStr = ctx.obj().getText();
//        JSONObject jsonObject = JSON.parseObject(objStr);
//        return input -> jsonObject;
//    }
//
//    @Override
//    public Evaluator<JSONObject, JSONObject> visitOCommon(CheckExprParser.OCommonContext ctx) {
//        Evaluator<JSONObject, ?> evaluator = visit(ctx.commonExpr());
//        return json -> (JSONObject) evaluator.eval(json);
//    }
//
//    /********** String *************/
//
//    @Override
//    public Evaluator<JSONObject, String> visitStr(CheckExprParser.StrContext ctx) {
//        String text = ctx.string().getText();
//        return input -> text.substring(1, text.length() - 1);
//    }
//
//    @Override
//    public Evaluator<JSONObject, String> visitSCommon(CheckExprParser.SCommonContext ctx) {
//        Evaluator<JSONObject, ?> evaluator = visit(ctx.commonExpr());
//        return json -> (String) evaluator.eval(json);
//    }
//}
