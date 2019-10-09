package com.legend.demo.controller;

import com.legend.annotation.Autowired;
import com.legend.annotation.Controller;
import com.legend.annotation.ModelAttribute;
import com.legend.annotation.RequestMapping;
import com.legend.demo.param.Result;
import com.legend.demo.service.RegisterService;
import com.legend.demo.service.UserService;
import com.legend.demo.model.User;
import com.legend.xml.rule.RequestMethod;

/**
 * @author Legend
 * @data by on 18-10-10.
 * @description
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result<Integer> register(@ModelAttribute("User") User user) {
        System.out.println("register--------");
        Integer integer = userService.createUser(user);
        return Result.success(integer);
    }
}
