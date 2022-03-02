package geektime.spring.springbucks.waiter.controller;

import geektime.spring.springbucks.waiter.model.Coffee;
import geektime.spring.springbucks.waiter.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

// 用于向spring容器中注入bean
@Controller
//用于 将特定HTTP请求方法 映射到 将要处理相应请求的控制器中的特定类或方法
//说明接收的请求：放在类上（类级别）相当于是个prefix，说明这里的请求都要以/coffee开头
@RequestMapping("/coffee")
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    //说明接收的请求：（方法级别）GET请求，路径为/
    @GetMapping("/")
    //说明返回体
    @ResponseBody
    public List<Coffee> getAll() {
        return coffeeService.getAllCoffee();
    }
}
