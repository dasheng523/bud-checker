package com.mengxinya.ys.common;

public interface Evaluator<T, R> {
    R eval(T input);
}
