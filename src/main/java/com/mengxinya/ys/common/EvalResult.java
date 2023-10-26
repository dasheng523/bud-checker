package com.mengxinya.ys.common;

public interface EvalResult<R> {
    CheckCode getCheckCode();

    String getMessage();

    R getData();
}
