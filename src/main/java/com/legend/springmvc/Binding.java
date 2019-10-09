package com.legend.springmvc;

import com.legend.annotation.ModelAttribute;
import com.legend.annotation.RequestParam;
import com.legend.exception.SpringMVCException;
import com.legend.utils.AnnotationUtils;
import com.legend.utils.TypeUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description 进行方法参数的绑定
 */
public class Binding {

    public static List<Object> bindingMethodParameters(Parameter[] parameters, HttpServletRequest request) {
        List<Object> requestParameters = new ArrayList<>();
        for (Parameter parameter: parameters) {
            try {
                Object requestParameter = bindingEachParameter(parameter, request);
                if (requestParameter != null) {
                    requestParameters.add(requestParameter);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return requestParameters;
    }

//    public static List<Object> bindingMethodParameters(Map<String, Method> bindingRequestMapping, HttpServletRequest request) {
//        List<Object> requestParameters = new ArrayList<>();
//        Set<Map.Entry<String, Method>> entries = bindingRequestMapping.entrySet();
//        for (Map.Entry<String, Method> entry: entries) {
//            Method method = entry.getValue();
//            Parameter[] parameters = method.getParameters();
//            for (Parameter parameter: parameters) {
//                // 参数有注解时进行处理
//                if (!AnnotationUtils.isEmpty(parameter.getAnnotations())) {
//                    Object requestParameter = null;
//                    try {
//                        requestParameter = bindingEachParameter(parameter, request);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                        throw new SpringMVCException("绑定参数异常");
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                        throw new SpringMVCException("绑定参数异常");
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                        throw new SpringMVCException("绑定参数异常");
//                    }
//                    requestParameters.add(requestParameter);
//                }
//            }
//        }
//        return requestParameters;
//    }

    private static Object bindingEachParameter(Parameter parameter, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        if (!AnnotationUtils.isEmpty(parameter.getAnnotation(RequestParam.class))) {
            BindParameter bindParameter = new BindingByRequestParam();
            Object resultParameter = bindParameter.bindingParameter(parameter, request);
            return resultParameter;
        } else if (!AnnotationUtils.isEmpty(parameter.getAnnotation(ModelAttribute.class))) {
            BindParameter bindParameter = new BindingByModelAttribute();
            Object resultParameter = bindParameter.bindingParameter(parameter, request);
            return resultParameter;
        } else if (parameter.getAnnotations()==null || parameter.getAnnotations().length==0) {
            boolean isBaseType = TypeUtils.isBasicType(parameter.getType().getSimpleName());
            // 如果是基本类型 直接交给BindingRequestParam进行装配
            if (isBaseType) {
                BindParameter bindParameter = new BindingByRequestParam();
                Object resultParameter = bindParameter.bindingParameter(parameter, request);
                return resultParameter;
            } else {
                // 如果是对象类型 则交给BindingByModelAttribute去进行装配
                BindParameter bindParameter = new BindingByModelAttribute();
                Object resultParameter = bindParameter.bindingParameter(parameter, request);
                return resultParameter;
            }
        }
        return null;
    }
}
