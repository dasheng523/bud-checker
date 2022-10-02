package com.mengxinya.ys.checker;

import java.util.List;

public class FunctionGetterMockImpl implements FunctionGetter {
    @Override
    public Evaluator<List<?>, ?> getFunction(String funcName) {
        if (funcName.equals("abs")) {
            return (Evaluator<List<?>, Double>) input -> Math.abs(Double.parseDouble(input.get(0).toString()));
        } else if (funcName.equals("isString")) {
            return (Evaluator<List<?>, Boolean>) input -> input.get(0) instanceof String;
        }
        throw new ExprSyntaxException("The funcName is not support: " + funcName);
    }
}
