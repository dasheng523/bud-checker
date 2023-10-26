package com.mengxinya.ys.checker;

import com.mengxinya.ys.parser.FunctionGetter;

public interface CustomCheckParams {
    FunctionGetter getFunctionGetter();

    String getExpr();
}
