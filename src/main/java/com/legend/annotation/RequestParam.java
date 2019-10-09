package com.legend.annotation;

import java.lang.annotation.*;

/**
 * @author Legend
 * @data by on 18-10-8.
 * @description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";
}
