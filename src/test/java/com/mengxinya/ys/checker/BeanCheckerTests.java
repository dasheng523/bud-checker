package com.mengxinya.ys.checker;

import com.mengxinya.ys.checker.beanchecker.*;
import com.mengxinya.ys.common.CheckResult;
import com.mengxinya.ys.common.Evaluator;
import com.mengxinya.ys.funcgetter.FunctionGetter;
import com.mengxinya.ys.funcgetter.FunctionGetterBaseImpl;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BeanCheckerTests {
    private final BeanChecker<TestBean> checker;

    public BeanCheckerTests() {
        FunctionGetter functionGetter = new FunctionGetterBaseImpl();
        FunctionGetter customGetter = funcName -> {
            if (funcName.equals("code")) {
                return (Evaluator<List<?>, Boolean>) input -> {
                    Object code = input.get(0);
                    Object name = input.get(1);
                    return code.equals("110010") && name.equals("广东省");
                };
            }
            return null;
        };
        checker = new BeanChecker<>(FunctionGetter.compose(List.of(functionGetter, customGetter)));
    }

    @Test
    void test1() {
        TestBean bean = new TestBean();
        bean.setAge(18);
        bean.setName("test");
        bean.setAddress("ffdafd asdfasd fasd asdf");
        bean.setPhone("18888888888");
        bean.setBirthday("2000-12-12");
        bean.setProvinceCode("110010");
        bean.setProvinceName("广东省");

        CheckResult<String> result = checker.eval(bean);
        Assertions.assertTrue(result.isValid());
    }

    @Test
    void test2() {
        TestBean bean = new TestBean();
        bean.setAge(5);
        bean.setName("a");
        bean.setAddress("aaaa");
        bean.setPhone("18888888888");
        bean.setBirthday("20001-12-12");
        bean.setProvinceCode("110010");
        bean.setProvinceName("广东省");

        CheckResult<String> result = checker.eval(bean);
        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals(result.getData().size(), 1);
        System.out.println(result.getMessage());
    }


    @Getter
    @Setter
    static class TestBean {
        @CheckExpr("${age} >= 0 and ${age} < 200")
        private Integer age;

        @CheckExpr("notnull(${name})")
        @CheckExpr("len(${name}) > 3 and len(${name}) < 50")
        private String name;

        @CheckExpr("len(${address}) < 200")
        private String address;

        private String phone;

        private String birthday;

        @CheckExpr(value = "code(${provinceCode}, ${provinceName})", msg = "省份不正确")
        private String provinceCode;

        private String provinceName;
    }
}
