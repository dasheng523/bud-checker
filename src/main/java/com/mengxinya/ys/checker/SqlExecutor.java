package com.mengxinya.ys.checker;

import java.util.List;
import java.util.Map;

public interface SqlExecutor {
    List<Map<String, Object>> query(String sql, Object... args);
}
