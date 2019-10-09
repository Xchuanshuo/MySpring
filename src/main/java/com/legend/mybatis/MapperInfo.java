package com.legend.mybatis;

import lombok.Data;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Data
public class MapperInfo {

    private String interfaceName;
    private String sqlContent;
    private String methodName;
    private IResultHandler resultHandler;
    private String sqlType;
}
