package com.legend.annotation;

import java.lang.annotation.*;

/**
 * @author Legend
 * @data by on 18-10-8.
 * @description
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER,
         ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
