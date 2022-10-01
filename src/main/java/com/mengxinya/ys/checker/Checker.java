package com.mengxinya.ys.checker;

public interface Checker<T> {
    CheckResult check(T input);
}
