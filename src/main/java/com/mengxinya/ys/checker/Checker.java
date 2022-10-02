package com.mengxinya.ys.checker;

import java.util.List;

public interface Checker<T> extends Evaluator<T, Boolean> {
    static <T> Checker<T> compose(List<Checker<T>> checkers) {
        return input -> {
            for (Checker<T> checker : checkers) {
                boolean rs = checker.eval(input);
                if (!rs) {
                    return false;
                }
            }
            return true;
        };
    }
}
