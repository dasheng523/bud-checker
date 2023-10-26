package com.mengxinya.ys.parser;

import com.mengxinya.ys.common.Evaluator;

import java.util.List;

public interface FunctionGetter {
    Evaluator<List<?>, ?> getFunction(String funcName);

    static FunctionGetter compose(List<FunctionGetter> getters) {
        return funcName -> {
            for (FunctionGetter getter : getters) {
                Evaluator<List<?>, ?> evaluator1 = getter.getFunction(funcName);
                if (evaluator1 != null) {
                    return evaluator1;
                }
            }
            return null;
        };
    }
}
