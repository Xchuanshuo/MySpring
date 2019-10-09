package com.legend.springmvc;

import com.legend.annotation.RequestParam;
import com.legend.exception.SpringMVCException;
import com.legend.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description 注解类型为RequestParam 直接进行绑定
 */
public class BindingByRequestParam implements BindParameter {

    @Override
    public Object bindingParameter(Parameter parameter, HttpServletRequest request) {
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        String requestParamValue = requestParam.value();
        String paramType = parameter.getType().getSimpleName();
        String param = request.getParameter(requestParamValue);
        if (StringUtils.isEmpty(param)) {
            throw new SpringMVCException("绑定参数异常");
        }
        if ("String".equals(paramType)) {
            return param;
        } else if ("Integer".equals(paramType) || "int".equals(paramType)) {
            return Integer.valueOf(param);
        }
        return param;
    }
}
