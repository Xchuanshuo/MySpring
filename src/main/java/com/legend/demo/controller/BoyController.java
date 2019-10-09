package com.legend.demo.controller;

import com.legend.annotation.Autowired;
import com.legend.annotation.Controller;
import com.legend.annotation.RequestMapping;
import com.legend.annotation.RequestParam;
import com.legend.demo.model.Boy;
import com.legend.demo.param.Result;
import com.legend.demo.service.BoyService;
import com.legend.utils.ListUtils;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-15.
 * @description
 */
@Controller
public class BoyController {

    @Autowired
    private BoyService boyService;

    @RequestMapping("/all_boy")
    public Result<List<Boy>> getAllBoy() {
        List<Boy> list = boyService.getAllBoy();
        return Result.success(list);
    }
}
