package com.mengxinya.ys.funcgetter;

import com.alibaba.fastjson.JSON;
import com.mengxinya.ys.common.Evaluator;

import java.util.List;

public class FunctionGetterBaseImpl implements FunctionGetter {
    @Override
    public Evaluator<List<?>, ?> getFunction(String funcName) {
        return switch (funcName) {
            case "abs" -> input -> Math.abs(Double.parseDouble(input.get(0).toString()));
            case "isString" -> input -> input.get(0) instanceof String;
            case "len" -> input -> input.get(0).toString().length();
            case "notnull", "notNull" -> input -> input.get(0) != null;
            case "isNull" -> input -> input.get(0) == null;
            case "regex" -> input -> input.get(0).toString().matches(input.get(1).toString());
            case "inArray" -> input -> input.subList(1, input.size()).contains(input.get(0));
            default -> null;
        };
    }
}
