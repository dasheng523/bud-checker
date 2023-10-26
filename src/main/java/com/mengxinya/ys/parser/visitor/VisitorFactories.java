package com.mengxinya.ys.parser.visitor;

import com.mengxinya.ys.parser.FunctionGetter;

import java.util.function.Function;

import static com.mengxinya.ys.checker.Functions.makeCacheFunction;

public class VisitorFactories {
    public static final Function<FunctionGetter, CommonExprVisitor> CommonExprVisitorFactory = makeCacheFunction(
            CommonExprVisitor::new
    );

    public static final Function<FunctionGetter, NumberExprVisitor> NumberExprVisitorFactory = makeCacheFunction(
            NumberExprVisitor::new
    );

    public static final Function<FunctionGetter, BooleanExprVisitor> BooleanExprVisitorFactory = makeCacheFunction(
            BooleanExprVisitor::new
    );

    public static final Function<FunctionGetter, StringExprVisitor> StringExprVisitorFactory = makeCacheFunction(
            StringExprVisitor::new
    );

    public static final Function<FunctionGetter, JsonExprVisitor> JsonExprVisitorFactory = makeCacheFunction(
            JsonExprVisitor::new
    );


}
