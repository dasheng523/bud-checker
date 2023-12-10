package com.mengxinya.ys.checker.beanchecker;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Inherited
@Repeatable(CheckExprs.class)
public @interface CheckExpr {
    String value();
    String msg() default "";
}
