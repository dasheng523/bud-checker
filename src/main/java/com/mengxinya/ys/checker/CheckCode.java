package com.mengxinya.ys.checker;

public enum CheckCode {
    // 有效
    VALID(1, "valid"),

    // 无效
    INVALID(2, "invalid");

    final int code;
    final String message;

    CheckCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
