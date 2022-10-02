package com.mengxinya.ys.checker;

import java.util.List;

public class FunctionGetterMockImpl implements FunctionGetter {
    @Override
    public Evaluator<List<?>, ?> getFunction(String funcName) {
        if (funcName.equals("abs")) {
            return (Evaluator<List<?>, Object>) input -> Math.abs((int)input.get(0));
        }
        throw new ExprSyntaxException("The funcName is not support: " + funcName);
    }
}
