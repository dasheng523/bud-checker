package com.mengxinya.ys.checker;

public interface Checkers {
    static <T> SimpleChecker<Comparable<T>> min(T value, String message) {
        return input -> CheckResult.make(input.compareTo(value) >= 0, message);
    }

    static <T> SimpleChecker<Comparable<T>> max(T value, String message) {
        return input -> CheckResult.make(input.compareTo(value) <= 0, message);
    }

    static <T> SimpleChecker<T> equals(T value, String message) {
        return input -> CheckResult.make(input.equals(value), message);
    }
}
