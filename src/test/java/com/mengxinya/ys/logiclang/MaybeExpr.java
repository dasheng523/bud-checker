package com.mengxinya.ys.logiclang;

public interface MaybeExpr extends LogicExpr {
    MaybeExpr nextChance() throws LogicLangException;
}
