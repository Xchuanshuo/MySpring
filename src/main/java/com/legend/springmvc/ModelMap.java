package com.legend.springmvc;

import java.util.LinkedHashMap;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
public class ModelMap extends LinkedHashMap<String, Object> implements Model {

    @Override
    public Model addAttribute(String attributeName, Object attributeValue) {
        put(attributeName, attributeValue);
        return this;
    }
}
