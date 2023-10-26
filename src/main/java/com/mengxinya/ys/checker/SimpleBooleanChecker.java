package com.mengxinya.ys.checker;

import antlr4.parser.expr.CheckExprLexer;
import antlr4.parser.expr.CheckExprParser;
import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.common.CheckResult;
import com.mengxinya.ys.common.Checker;
import com.mengxinya.ys.common.Evaluator;
import com.mengxinya.ys.parser.FunctionGetter;
import com.mengxinya.ys.parser.visitor.CommonExprVisitor;
import com.mengxinya.ys.parser.visitor.VisitorFactories;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class SimpleBooleanChecker implements Checker<JSONObject, Void> {
    private final Evaluator<JSONObject, ?> evaluator;

    public SimpleBooleanChecker(FunctionGetter functionGetter, String expr) {
        CommonExprVisitor commonExprVisitor = VisitorFactories.CommonExprVisitorFactory.apply(functionGetter);

        CharStream input = CharStreams.fromString(expr);
        CheckExprLexer lexer = new CheckExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CheckExprParser parser = new CheckExprParser(tokens);
        CheckExprParser.ExprContext tree = parser.expr();
        this.evaluator = commonExprVisitor.visit(tree);
    }

    @Override
    public CheckResult<Void> eval(JSONObject input) {
        boolean result = (boolean)evaluator.eval(input);
        return CheckResult.make(result);
    }
}
