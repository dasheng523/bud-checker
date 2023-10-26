package com.mengxinya.ys.parser.visitor;

import antlr4.parser.expr.CheckExprBaseVisitor;
import com.mengxinya.ys.parser.FunctionGetter;
import com.mengxinya.ys.common.SimpleChecker;

public class SimpleCheckerExprVisitor<T> extends CheckExprBaseVisitor<SimpleChecker<T>> {
    private final CommonExprVisitor visitor;

    public SimpleCheckerExprVisitor(FunctionGetter functionGetter) {
        this.visitor = new CommonExprVisitor(functionGetter);
    }
}
