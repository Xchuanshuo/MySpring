package com.legend.springmvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
public class BindingRequestAndModel {

    /**
     * 遍历ModelMap 将Model中的数据绑定到request中
     * @param modelAndView
     * @param request
     */
    public static void bindingRequestAndModel(ModelAndView modelAndView, HttpServletRequest request) {
        ModelMap modelMap = modelAndView.getModelMap();
        if (!modelMap.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = modelMap.entrySet();
            for (Map.Entry<String, Object> entry: entries) {
                String key = entry.getKey();
                Object value = entry.getValue();
                request.setAttribute(key, value);
            }
        }
    }
}
