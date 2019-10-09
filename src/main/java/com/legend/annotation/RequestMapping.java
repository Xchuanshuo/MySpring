package com.legend.annotation;

import com.legend.xml.rule.RequestMethod;

import java.lang.annotation.*;

/**
 * @author Legend
 * @data by on 18-10-8.
 * @description
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String value() default "";
    RequestMethod[] method() default {};
}
