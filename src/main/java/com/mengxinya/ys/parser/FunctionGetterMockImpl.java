package com.mengxinya.ys.parser;

import com.mengxinya.ys.checker.ExprSyntaxException;
import com.mengxinya.ys.common.Evaluator;

import java.util.List;

public class FunctionGetterMockImpl implements FunctionGetter {
    @Override
    public Evaluator<List<?>, ?> getFunction(String funcName) {
        if (funcName.equals("abs")) {
            return (Evaluator<List<?>, Double>) input -> Math.abs(Double.parseDouble(input.get(0).toString()));
        } else if (funcName.equals("isString")) {
            return (Evaluator<List<?>, Boolean>) input -> input.get(0) instanceof String;
        } else if (funcName.equals("len")) {
            return (Evaluator<List<?>, Integer>) input -> input.get(0).toString().length();
        }
        return null;
    }
}
