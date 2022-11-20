package com.mengxinya.ys.logiclang;

public interface LogicExpr {
    void execute(Context context) throws LogicLangException;
}
