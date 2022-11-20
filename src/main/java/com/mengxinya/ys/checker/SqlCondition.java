package com.mengxinya.ys.checker;

import java.util.Map;

public interface SqlCondition {
    String getCondition();
    Map<String, Object> getParams();
}
