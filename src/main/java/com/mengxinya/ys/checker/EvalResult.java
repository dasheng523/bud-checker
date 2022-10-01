package com.mengxinya.ys.checker;

public interface EvalResult<R> {
    CheckCode getCheckCode();

    String getMessage();

    R getData();
}
