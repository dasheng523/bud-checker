package com.mengxinya.ys.checker.beanchecker;

import com.alibaba.fastjson.JSONObject;
import com.mengxinya.ys.checker.SimpleJsonChecker;
import com.mengxinya.ys.common.CheckResult;
import com.mengxinya.ys.common.Checker;
import com.mengxinya.ys.common.CheckerUtils;
import com.mengxinya.ys.funcgetter.FunctionGetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BeanChecker implements Checker<Object, String> {

    protected final FunctionGetter functionGetter;

    public BeanChecker(FunctionGetter functionGetter) {
        this.functionGetter = functionGetter;
    }


    @Override
    public CheckResult<String> eval(Object input) {
        Class<?> tClass = input.getClass();
        Checker<JSONObject, String> checker = toClassChecker(tClass);
        return checker.eval((JSONObject)JSONObject.toJSON(input));
    }

    protected Checker<JSONObject, String> toClassChecker(Class<?> tClass) {
        // 获取所有CheckExpr注解字段
        List<Field> fieldList = findFields(tClass);
        // 分别将这些字段转换成checker
        List<Checker<JSONObject, String>> checkers = fieldList.stream().map(this::fieldToChecker).toList();
        // 合并这些checker，并返回
        return CheckerUtils.compose(checkers);
    }


    protected Checker<JSONObject, String> fieldToChecker(Field field) {
        String name = field.getName();
        CheckExpr[] checkExprs = field.getAnnotationsByType(CheckExpr.class);

        List<Checker<JSONObject, String>> checkers = Arrays.stream(checkExprs).map(checkExpr -> {
            String expr = checkExpr.value().trim().replace("${this}", "${" + name + "}");  // 替换$this关键字
            SimpleJsonChecker booleanChecker = new SimpleJsonChecker(functionGetter, expr);
            return (Checker<JSONObject, String>) input -> {
                CheckResult<Void> result = booleanChecker.eval(input);
                if (result.isValid()) {
                    return CheckResult.make(result.isValid());
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
        }).toList();

        return CheckerUtils.compose(checkers);
    }

    protected List<Field> findFields(Class<?> tClass) {
        return Arrays.stream(tClass.getDeclaredFields()).filter(field -> field.getAnnotationsByType(CheckExpr.class).length > 0).toList();
    }
}
