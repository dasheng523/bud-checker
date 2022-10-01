package com.mengxinya.ys.checker;

import java.util.List;

public interface CheckResult extends EvalResult<List<?>> {
    static CheckResult successResult(String message) {
        return new CheckResult() {
            @Override
            public CheckCode getCheckCode() {
                return CheckCode.VALID;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public List<?> getData() {
                return List.of();
            }
        };
    }

    static CheckResult failResult(String message) {
        return failResult(message, List.of());
    }

    static CheckResult failResult(String message, List<Object> data) {
        return new CheckResult() {
            @Override
            public CheckCode getCheckCode() {
                return CheckCode.INVALID;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public List<?> getData() {
                return data;
            }
        };
    }
}
