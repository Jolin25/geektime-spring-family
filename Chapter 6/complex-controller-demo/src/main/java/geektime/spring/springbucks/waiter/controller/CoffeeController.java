package geektime.spring.springbucks.waiter.controller;

import geektime.spring.springbucks.waiter.model.Coffee;
import geektime.spring.springbucks.waiter.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/coffee")
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;
    //http://localhost:8080/coffee/?price=11
    @GetMapping(path = "/", params = "!name")
    @ResponseBody
    public List<Coffee> getAll() {
        return coffeeService.getAllCoffee();
    }
    //http://localhost:8080/coffee/1
    @RequestMapping(path = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Coffee getById(@PathVariable Long id) {
        Coffee coffee = coffeeService.getCoffee(id);
        return coffee;
    }
    //http://localhost:8080/coffee/?name=espresso&price=11
    @GetMapping(path = "/")
    @ResponseBody
    public Coffee getByName(@RequestParam("price") String name,@RequestParam("name") String price) {
        return coffeeService.getCoffee(name);
    }
}
