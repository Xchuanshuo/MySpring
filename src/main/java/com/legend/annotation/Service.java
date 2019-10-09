package com.legend.annotation;

import java.lang.annotation.*;

/**
 * @author Legend
 * @data by on 18-10-8.
 * @description
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    String value() default "";
}
