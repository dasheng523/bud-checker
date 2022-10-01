package com.mengxinya.ys.checker;

public enum CheckCode {
    // 有效
    VALID(1),

    // 无效
    INVALID(2);

    final int code;

    CheckCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
