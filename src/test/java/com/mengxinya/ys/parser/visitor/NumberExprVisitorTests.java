package com.mengxinya.ys.parser.visitor;

import antlr4.parser.expr.CheckExprLexer;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.common.Evaluator;
import com.mengxinya.ys.parser.FunctionGetter;
import com.mengxinya.ys.parser.FunctionGetterMockImpl;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberExprVisitorTests {

    FunctionGetter functionGetter = new FunctionGetterMockImpl();
    private final NumberExprVisitor numberExprVisitor = VisitorFactories.NumberExprVisitorFactory.apply(functionGetter);

    @Test
    void testSimpleExpr1() {
        String expr = "1";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(null);
        Assertions.assertEquals(1, rs);
    }

    @Test
    void testSimpleExpr2() {
        String expr = "0";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(null);
        Assertions.assertEquals(0, rs);
    }

    @Test
    void testSimpleExpr3() {
        String expr = "1.1";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(null);
        Assertions.assertEquals(1.1, rs);
    }

    @Test
    void testSimpleExpr4() {
        String expr = "1.";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(null);
        Assertions.assertEquals(1.0, rs);
    }

    @Test
    void testSimpleHolder1() {
        String expr = "${price}";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 1.0}"));
        Assertions.assertEquals(1.0, rs);
    }

    @Test
    void testSimpleHolder2() {
        String expr = "${order.price}";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"order\": {\"name\": \"YS\", \"price\": \"1.0\"}}"));
        Assertions.assertEquals(1.0, rs);
    }

    @Test
    void testSimpleHolder3() {
        String expr = "${order.1}";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"order\": [1.0, 2.0]}"));
        Assertions.assertEquals(2.0, rs);
    }


    @Test
    void testSimpleHolder4() {
        String expr = "${orders.1.price}";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"orders\": [{\"name\": \"YS1\", \"price\": \"1.0\"}, {\"name\": \"YS2\", \"price\": \"2.0\"}]}"));
        Assertions.assertEquals(2.0, rs);
    }

    @Test
    void testOp1() {
        String expr = "${order.0} * ${order.1}";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"order\": [1.1, 2.0]}"));
        Assertions.assertEquals(2.2, rs);
    }

    @Test
    void testOp2() {
        String expr = "${order.0} + ${order.1}";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"order\": [1.1, 2.0]}"));
        Assertions.assertEquals(3.1, rs);
    }

    @Test
    void testOp3() {
        String expr = "${order.1} - 1.0";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"order\": [1.1, 2.0]}"));
        Assertions.assertEquals(1, rs);
    }

    @Test
    void testOp4() {
        String expr = "${order.0} / 1.1";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"order\": [1.1, 2.0]}"));
        Assertions.assertEquals(1, rs);
    }

    @Test
    void testComplexExpr1() {
        String expr = "(5 + ${order.0} / (1.1 - 0.1))*2 - ${order.1}";
        Evaluator<JSONObject, Double> evaluator = eval(expr);
        Double rs = evaluator.eval(JSON.parseObject("{\"order\": [1.1, 2.0]}"));
        Assertions.assertEquals(10.2, rs);
    }

    private Evaluator<JSONObject, Double> eval(String expr) {
        CharStream input = CharStreams.fromString(expr);
        CheckExprLexer lexer=new CheckExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CheckExprParser parser = new CheckExprParser(tokens);
        CheckExprParser.NumberExprContext tree = parser.numberExpr();
        return numberExprVisitor.visit(tree);
    }
}
