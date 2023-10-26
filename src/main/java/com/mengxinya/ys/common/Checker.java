package com.mengxinya.ys.common;

import java.util.List;

public interface Checker<T, R> extends Evaluator<T, CheckResult<R>> {
    static <T, R> Checker<T, R> and(List<Checker<T, R>> checkers) {
        return input -> {
            for (Checker<T, R> checker : checkers) {
                CheckResult<R> rs = checker.eval(input);
                if (!rs.isValid()) {
                    return rs;
                }
            }
            return CheckResult.make(true);
        };
    }

    static <T, R> Checker<T, R> or(List<Checker<T, R>> checkers) {
        return input -> {
            for (Checker<T, R> checker : checkers) {
                CheckResult<R> rs = checker.eval(input);
                if (rs.isValid()) {
                    return rs;
                }
            }
            return CheckResult.make(false);
        };
    }

    static <T, R> Checker<T, R> not(Checker<T, R> checker) {
        return input -> {
            CheckResult<R> rs = checker.eval(input);
            if (rs.getCheckCode() == CheckCode.INVALID) {
                return CheckResult.make(true);
            }
            return CheckResult.make(false);
        };
    }
}
