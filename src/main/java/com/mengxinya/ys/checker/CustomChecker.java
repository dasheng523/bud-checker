package com.mengxinya.ys.checker;

import antlr4.parser.expr.CheckExprLexer;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.visitor.BooleanExprVisitor;
import com.mengxinya.ys.checker.visitor.VisitorFactories;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class CustomChecker implements Checker<JSONObject> {

    private final Evaluator<JSONObject, Boolean> evaluator;

    public CustomChecker(CustomCheckParams params) {
        FunctionGetter functionGetter = params.getFunctionGetter();
        String expr = params.getExpr();
        BooleanExprVisitor visitor = VisitorFactories.BooleanExprVisitorFactory.apply(functionGetter);
        CharStream input = CharStreams.fromString(expr);
        CheckExprLexer lexer = new CheckExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CheckExprParser parser = new CheckExprParser(tokens);
        CheckExprParser.ExprContext tree = parser.expr();
        this.evaluator = visitor.visit(tree);
    }

    @Override
    public Boolean eval(JSONObject input) {
        return evaluator.eval(input);
    }
}
