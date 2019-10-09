package com.legend.springmvc;

import com.legend.annotation.ModelAttribute;
import com.legend.exception.SpringMVCException;
import com.legend.utils.AnnotationUtils;
import com.legend.utils.FieldUtils;
import com.legend.utils.StringUtils;
import com.legend.utils.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description 参数注解是ModelAttribute 绑定数据的类
 */
@Slf4j
public class BindingByModelAttribute implements BindParameter {

    @Override
    public Object bindingParameter(Parameter parameter, HttpServletRequest request) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        ModelAttribute modelAttribute = parameter.getAnnotation(ModelAttribute.class);
        Class<?> clazz = parameter.getType();
        if (!AnnotationUtils.isEmpty(modelAttribute)) {
            if (!clazz.getSimpleName().equals(modelAttribute.value())) {
                throw new SpringMVCException("实体类绑定异常，请重新检查");
            }
        }
        Field[] fields = clazz.getDeclaredFields();
        Object object = null;
        try {
            object = clazz.getConstructor().newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        for (Field field: fields) {
            String param = request.getParameter(field.getName());
            if (!StringUtils.isEmpty(param)) {
                // 具体的绑定 1.参数值 2.方法名 3.反射调用set方法
                Object setObject = TypeUtils.convert(field.getType().getSimpleName(), param);
                String methodName = FieldUtils.getSetMethodNameByNameField(field.getName());
                Method method = clazz.getMethod(methodName, field.getType());
                try {
                    method.invoke(object, setObject);
                } catch (InvocationTargetException e) {
                    log.error("{}属性赋值异常", field.getName());
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
}
