package com.mengxinya.ys.checker.visitor;

import antlr4.parser.expr.CheckExprLexer;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.Evaluator;
import com.mengxinya.ys.checker.FunctionGetter;
import com.mengxinya.ys.checker.FunctionGetterMockImpl;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommonExprVisitorTests {
    FunctionGetter functionGetter = new FunctionGetterMockImpl();
    CommonExprVisitor commonExprVisitor = VisitorFactories.CommonExprVisitorFactory.apply(functionGetter);

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


    private Evaluator<JSONObject, ?> eval(String expr) {
        CharStream input = CharStreams.fromString(expr);
        CheckExprLexer lexer = new CheckExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CheckExprParser parser = new CheckExprParser(tokens);
        CheckExprParser.ExprContext tree = parser.expr();
        return commonExprVisitor.visit(tree);
    }
}
