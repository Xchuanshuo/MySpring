package com.legend.springmvc;

import lombok.Data;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description 视图解析器 前缀和后缀
 */
@Data
public class ViewResolver {

    private String prefix = "";
    private String suffix = "";
}
