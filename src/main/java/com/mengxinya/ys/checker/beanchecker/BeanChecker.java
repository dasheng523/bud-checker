package com.mengxinya.ys.checker.beanchecker;

import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.SimpleBooleanChecker;
import com.mengxinya.ys.common.CheckResult;
import com.mengxinya.ys.common.Checker;
import com.mengxinya.ys.common.CheckerUtils;
import com.mengxinya.ys.parser.FunctionGetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BeanChecker<T> implements Checker<T, String> {

    private final FunctionGetter functionGetter;

    public BeanChecker(FunctionGetter functionGetter) {
        this.functionGetter = functionGetter;
    }


    @Override
    public CheckResult<String> eval(T input) {
        Class<?> tClass = input.getClass();
        Checker<JSONObject, String> checker = toClassChecker(tClass);
        return checker.eval((JSONObject)JSONObject.toJSON(input));
    }

    private Checker<JSONObject, String> toClassChecker(Class<?> tClass) {
        // 获取所有CheckExpr注解字段
        List<Field> fieldList = findFields(tClass);
        // 分别将这些字段转换成checker
        List<Checker<JSONObject, String>> checkers = fieldList.stream().map(this::fieldToChecker).toList();
        // 合并这些checker，并返回
        return CheckerUtils.compose(checkers);
    }

    private Checker<JSONObject, String> fieldToChecker(Field field) {
        String name = field.getName();
        CheckExpr checkExpr = field.getAnnotation(CheckExpr.class);

        // 替换$this关键字
        String expr = checkExpr.value().trim().replace("${this}", "${" + name + "}");
        SimpleBooleanChecker booleanChecker = new SimpleBooleanChecker(functionGetter, expr);

        return input -> {
            CheckResult<Void> result = booleanChecker.eval(input);
            if (result.isValid()) {
                return CheckResult.make(true);
            }
            else {
                String msg;
                if (checkExpr.msg().isEmpty()) {
                    msg = name + "校验不通过";
                }
                else {
                    msg = checkExpr.msg();
                }

                return CheckResult.make(false, msg, List.of(msg));
            }
        };
    }

    private List<Field> findFields(Class<?> tClass) {
        return Arrays.stream(tClass.getFields()).filter(field -> field.isAnnotationPresent(CheckExpr.class)).toList();
    }
}
