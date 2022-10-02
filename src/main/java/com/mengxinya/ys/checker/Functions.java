package com.mengxinya.ys.checker;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Functions {

    public static <T, R> Function<T, R> makeCacheFunction(Function<T, R> function) {
        return new Function<>() {
            final Map<T, R> cache = new HashMap<>();
            @Override
            public R apply(T key) {
                if (cache.containsKey(key)) {
                    return cache.get(key);
                }
                R val = function.apply(key);
                cache.put(key, val);
                return val;
            }
        };
    }
}
