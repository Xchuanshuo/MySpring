package com.legend.annotation;

import java.lang.annotation.*;

/**
 * @author Legend
 * @data by on 18-10-8.
 * @description 绑定对象用的注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModelAttribute {

    String value() default "";
}
