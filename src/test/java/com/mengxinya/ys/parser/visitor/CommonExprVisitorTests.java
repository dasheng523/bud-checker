package com.mengxinya.ys.parser.visitor;

import antlr4.parser.expr.CheckExprLexer;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.common.Evaluator;
import com.mengxinya.ys.funcgetter.FunctionGetter;
import com.mengxinya.ys.funcgetter.FunctionGetterBaseImpl;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommonExprVisitorTests {
    FunctionGetter functionGetter = new FunctionGetterBaseImpl();
    CommonExprVisitor commonExprVisitor = new CommonExprVisitor(functionGetter);

    @Test
    void testBooleanExpr1() {
        String expr = "true";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        boolean rs = (boolean)evaluator.eval(null);
        Assertions.assertTrue(rs);
    }

    @Test
    void testBooleanExpr2() {
        String expr = "false";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        boolean rs = (boolean)evaluator.eval(null);
        Assertions.assertFalse(rs);
    }

    @Test
    void testBHolder() {
        String expr = "${isDel}";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        boolean rs = (boolean)evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"isDel\": true}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOpExpr1() {
        String expr = "${price} > 0";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        boolean rs = (boolean)evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testSimpleExpr1() {
        String expr = """
                "test"
                """;
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Object rs = evaluator.eval(null);
        Assertions.assertEquals("test", rs.toString());
    }

    @Test
    void testSimpleExpr2() {
        String expr = """
                {"name": "test"}
                """;
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        JSONObject rs = (JSONObject)evaluator.eval(null);
        Assertions.assertEquals("test", rs.get("name"));
    }

    @Test
    void testSimpleNumber() {
        String expr = "-10";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Double rs = (Double) evaluator.eval(null);
        Assertions.assertEquals(-10, rs);
    }

    @Test
    void testSimpleNumber2() {
        String expr = "(-10)";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Double rs = (Double) evaluator.eval(null);
        Assertions.assertEquals(-10, rs);
    }

    @Test
    void testOp1() {
        String expr = "${order.0} * ${order.1}";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Double rs = (Double)evaluator.eval(JSON.parseObject("{\"order\": [1.1, 2.0]}"));
        Assertions.assertEquals(2.2, rs);
    }

    @Test
    void testOp2() {
        String expr = "${order.0} + ${order.1}";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Double rs = (Double)evaluator.eval(JSON.parseObject("{\"order\": [1.1, 2.0]}"));
        Assertions.assertEquals(3.1, rs);
    }

    @Test
    void testOp3() {
        String expr = "5 / 2";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Double rs = (Double)evaluator.eval(JSON.parseObject(null));
        Assertions.assertEquals(2.5, rs);
    }

    @Test
    void testFunction1() {
        String expr = """
                abs(-1)
                """;
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Double rs = (Double)evaluator.eval(null);
        Assertions.assertEquals(1, rs);
    }

    @Test
    void testFunction2() {
        String expr = """
                isString("test")
                """;
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Boolean rs = (Boolean)evaluator.eval(null);
        Assertions.assertTrue(rs);
    }

    @Test
    void testFunction3() {
        String expr = "isString(${order.0})";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        Boolean rs = (Boolean)evaluator.eval(JSON.parseObject("{\"order\": [\"aaa\", 2.0]}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOr1() {
        String expr = "(${price} > 0 or false)";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        boolean rs = (Boolean)evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOr2() {
        String expr = "(${price} > 5 and true or 4>3)";
        Evaluator<JSONObject, ?> evaluator = eval(expr);
        boolean rs = (Boolean)evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }


    private Evaluator<JSONObject, ?> eval(String expr) {
        CharStream input = CharStreams.fromString(expr);
        CheckExprLexer lexer = new CheckExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CheckExprParser parser = new CheckExprParser(tokens);
        CheckExprParser.ExprContext tree = parser.expr();
        return commonExprVisitor.visit(tree);
    }
}
