package com.mengxinya.ys.common;

import java.util.ArrayList;
import java.util.List;

public interface CheckResult<T> extends EvalResult<List<T>> {

    boolean isValid();

    static <T> CheckResult<T> make(boolean valid) {
        CheckCode code = valid ? CheckCode.VALID : CheckCode.INVALID;
        return make(valid, code.getMessage());
    }

    static <T> CheckResult<T> make(boolean valid, String message) {
        return make(valid, message, new ArrayList<>());
    }

    static <T> CheckResult<T> make(boolean valid, String message, List<T> data) {
        return new CheckResult<>() {
            @Override
            public boolean isValid() {
                return valid;
            }

            @Override
            public CheckCode getCheckCode() {
                return valid ? CheckCode.VALID : CheckCode.INVALID;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public List<T> getData() {
                return data;
            }
        };
    }
}
