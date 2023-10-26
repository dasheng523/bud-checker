package com.mengxinya.ys.checker.parser;

import antlr4.parser.expr.CheckExprLexer;
import antlr4.parser.expr.CheckExprParser;
import com.mengxinya.ys.checker.FunctionGetter;
import com.mengxinya.ys.checker.SimpleChecker;
import com.mengxinya.ys.checker.visitor.SimpleCheckerExprVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public interface SimpleCheckerParser {
    <T> SimpleChecker<T> eval(String expr);

    static SimpleCheckerParser make(FunctionGetter functionGetter) {
        return new SimpleCheckerParser() {
            @Override
            public <T> SimpleChecker<T> eval(String expr) {
                CharStream input = CharStreams.fromString(expr);
                CheckExprLexer lexer=new CheckExprLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                CheckExprParser parser = new CheckExprParser(tokens);
                CheckExprParser.BooleanExprContext tree = parser.booleanExpr();
                SimpleCheckerExprVisitor<T> visitor = new SimpleCheckerExprVisitor<>(functionGetter);
                return visitor.visit(tree);
            }
        };
    }

}
