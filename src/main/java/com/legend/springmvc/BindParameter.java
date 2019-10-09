package com.legend.springmvc;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
public interface BindParameter {

    Object bindingParameter(Parameter parameter, HttpServletRequest request) throws IllegalAccessException, InstantiationException, NoSuchMethodException;
}
