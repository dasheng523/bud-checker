package com.mengxinya.ys.checker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CheckerUtilsTests {
    static JSONObject jsonObject = JSON.parseObject("{\"orders\": [{\"name\": \"YS1\", \"price\": 1.0}, {\"name\": \"YS2\", \"price\": 2.0}]}");

    @Test
    void testGetJsonValue1() {
        Object val = CheckerUtils.getJsonValue(jsonObject, "orders.1.price");
        Assertions.assertEquals(BigDecimal.valueOf(2.0), val);
    }

    @Test
    void testGetJsonValue2() {
        Object val = CheckerUtils.getJsonValue(jsonObject, "orders.1.name");
        Assertions.assertEquals("YS2", val);
    }
}
