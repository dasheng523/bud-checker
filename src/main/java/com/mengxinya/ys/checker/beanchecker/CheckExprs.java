package com.mengxinya.ys.checker.beanchecker;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface CheckExprs {
    CheckExpr[] value();
}
