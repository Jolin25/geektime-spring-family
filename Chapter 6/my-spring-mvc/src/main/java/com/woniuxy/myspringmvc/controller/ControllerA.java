package com.woniuxy.myspringmvc.controller;

import com.woniuxy.myspringmvc.annotation.Controller;
import com.woniuxy.myspringmvc.annotation.RequestMapping;
import com.woniuxy.myspringmvc.annotation.ResponseBody;
import com.woniuxy.myspringmvc.pojo.Person;

/**
 * @author jrl
 * @date Create in 09:26 2022-3-15
 */
@RequestMapping("/controller")
@Controller
public class ControllerA {
    @RequestMapping("/test1")
    @ResponseBody
    public Person testController(String name, int age) {
        Person p = new Person(name, age);
        return p;
    }

    @RequestMapping("/test2")
    @ResponseBody
    public Person testController2() {
        Person p = new Person("mona", 10);
        return p;
    }
}
