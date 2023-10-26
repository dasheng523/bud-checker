package com.mengxinya.ys.checker.beanchecker;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface CheckExpr {
    String value();
    String msg() default "";
}
