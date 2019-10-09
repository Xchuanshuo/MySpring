package com.legend.demo.controller;

import com.legend.annotation.*;
import com.legend.demo.param.CodeMsg;
import com.legend.demo.param.Result;
import com.legend.demo.service.UserService;
import com.legend.demo.model.User;
import com.legend.springmvc.ModelAndView;
import com.legend.springmvc.ModelMap;
import com.legend.xml.rule.RequestMethod;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-10.
 * @description
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("User") User user) {
        ModelAndView modelAndView = new ModelAndView("success");
        ModelMap modelMap = new ModelMap();
        User user1 = userService.queryUser(user.getUsername(), user.getPassword());
        if (user1.getUid() == null) {
            modelAndView.setView("failure");
            modelMap.addAttribute("test", CodeMsg.PASSWORD_ERROR);
        } else {
            modelMap.addAttribute("test", user1.getUsername());
        }
        modelAndView.setModelMap(modelMap);
        return modelAndView;
    }

    @RequestMapping("/hello2")
    public Result<User> test() {
        User user = new User();
        user.setUid(666666);
        user.setUsername("Legend");
        user.setPassword("66666666");
        return Result.success(user);
    }

    @RequestMapping("/hello1")
    public Result<Object> success() {
        return Result.error(CodeMsg.BIND_ERROR);
    }

    @RequestMapping("/all_user")
    public Result<List<User>> getAllUser() {
        List<User> list = userService.queryAllUser();
        return Result.success(list);
    }

    @RequestMapping(value = "/update_user", method = RequestMethod.PUT)
    public Result<Integer> updateUser(@ModelAttribute("User") User user) {
        Integer result = userService.updateUserById(user);
        return Result.success(result);
    }

    @RequestMapping(value = "/delete_user", method = RequestMethod.DELETE)
    public Result<Integer> deleteUser(@RequestParam("uid") int id) {
        Integer result = userService.deleteUserById(id);
        return Result.success(result);
    }

}
