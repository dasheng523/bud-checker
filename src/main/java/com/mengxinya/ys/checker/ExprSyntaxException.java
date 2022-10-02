package com.mengxinya.ys.checker;

public class ExprSyntaxException extends RuntimeException{
    public ExprSyntaxException(String msg) {
        super(msg);
    }

    public ExprSyntaxException() {
        super();
    }
}
