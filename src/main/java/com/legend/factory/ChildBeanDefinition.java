package com.legend.factory;

import lombok.Builder;
import lombok.Data;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description
 */
@Data
@Builder
public class ChildBeanDefinition {

    private String childType;
    private String attributeOne;
    private String attributeTwo;
}
