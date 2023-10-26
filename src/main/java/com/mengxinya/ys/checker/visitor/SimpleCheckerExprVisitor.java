package com.mengxinya.ys.checker.visitor;

import antlr4.parser.expr.CheckExprBaseVisitor;
import com.mengxinya.ys.checker.FunctionGetter;
import com.mengxinya.ys.checker.SimpleChecker;

public class SimpleCheckerExprVisitor<T> extends CheckExprBaseVisitor<SimpleChecker<T>> {
    private final CommonExprVisitor visitor;

    public SimpleCheckerExprVisitor(FunctionGetter functionGetter) {
        this.visitor = new CommonExprVisitor(functionGetter);
    }
}
