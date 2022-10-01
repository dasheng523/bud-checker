package com.mengxinya.ys.checker;

public interface Evaluator<T, R> {
    R eval(T input);
}
