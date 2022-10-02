package com.mengxinya.ys.checker.visitor;

import com.mengxinya.ys.checker.FunctionGetter;

import java.util.function.Function;

import static com.mengxinya.ys.checker.Functions.makeCacheFunction;

public class VisitorFactories {
    public static final Function<FunctionGetter, CommonExprVisitor> CommonExprVisitorFactory = makeCacheFunction(
            CommonExprVisitor::new
    );

}
