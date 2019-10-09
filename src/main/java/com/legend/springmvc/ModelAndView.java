package com.legend.springmvc;

import lombok.Data;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Data
public class ModelAndView {

    private String view;
    private ModelMap modelMap;

    public ModelAndView(String view) {
        this.view = view;
    }
}
