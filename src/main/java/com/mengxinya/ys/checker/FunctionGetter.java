package com.mengxinya.ys.checker;

import java.util.List;

public interface FunctionGetter {
    Evaluator<List<?>, ?> getFunction(String funcName);
}
