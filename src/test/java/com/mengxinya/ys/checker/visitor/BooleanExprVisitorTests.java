package com.mengxinya.ys.checker.visitor;

import antlr4.parser.expr.CheckExprLexer;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.Evaluator;
import com.mengxinya.ys.checker.FunctionGetter;
import com.mengxinya.ys.checker.FunctionGetterMockImpl;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BooleanExprVisitorTests {
    FunctionGetter functionGetter = new FunctionGetterMockImpl();
    BooleanExprVisitor booleanExprVisitor = VisitorFactories.BooleanExprVisitorFactory.apply(functionGetter);

    @Test
    void testSimpleExpr1() {
        String expr = "true";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(null);
        Assertions.assertTrue(rs);
    }

    @Test
    void testSimpleExpr2() {
        String expr = "false";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(null);
        Assertions.assertFalse(rs);
    }

    @Test
    void testBHolder() {
        String expr = "${isDel}";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"isDel\": true}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOpExpr1() {
        String expr = "${price} > 0";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOpExpr2() {
        String expr = "${price} >=5";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOpExpr3() {
        String expr = "${price} <=5*${price}";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOpExpr4() {
        String expr = "${price} !=5*${price}";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOpExpr5() {
        String expr = "${price}/5.0 ==4.0/4.0";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testBBrackets1() {
        String expr = "(true)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testBBrackets2() {
        String expr = "(1==1+0)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testNot() {
        String expr = "!(1==1+1)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testAnd0() {
        String expr = "!(true and false)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testAnd1() {
        String expr = "!(!(1==1+1) and false)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testAnd2() {
        String expr = "!(${price} > 0 and false)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOr1() {
        String expr = "(${price} > 0 or false)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    @Test
    void testOr2() {
        String expr = "(${price} > 5 and true or 4>3)";
        Evaluator<JSONObject, Boolean> evaluator = eval(expr);
        boolean rs = evaluator.eval(JSON.parseObject("{\"name\": \"YS\", \"price\": 5.0}"));
        Assertions.assertTrue(rs);
    }

    private Evaluator<JSONObject, Boolean> eval(String expr) {
        CharStream input = CharStreams.fromString(expr);
        CheckExprLexer lexer = new CheckExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CheckExprParser parser = new CheckExprParser(tokens);
        CheckExprParser.BooleanExprContext tree = parser.booleanExpr();
        return booleanExprVisitor.visit(tree);
    }
}
