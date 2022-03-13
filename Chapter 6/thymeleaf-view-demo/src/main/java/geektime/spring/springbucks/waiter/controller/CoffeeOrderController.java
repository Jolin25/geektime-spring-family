package geektime.spring.springbucks.waiter.controller;

import geektime.spring.springbucks.waiter.controller.request.NewOrderRequest;
import geektime.spring.springbucks.waiter.model.Coffee;
import geektime.spring.springbucks.waiter.model.CoffeeOrder;
import geektime.spring.springbucks.waiter.service.CoffeeOrderService;
import geektime.spring.springbucks.waiter.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * knowledge point:  这里用的Controller而不是RestController是因为
 * 这个类下面有的方法返回的是ModelAndView，这不是REST风格
 */
// TODO_Joly:REST是什么
@Controller
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {
    @Autowired
    private CoffeeOrderService orderService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/{id}")
    /** knowledge point:  因为类上面用的Controller而不是RestController，所以ResponseBody就不能省掉了*/
    @ResponseBody
    public CoffeeOrder getOrder(@PathVariable("id") Long id) {
        return orderService.get(id);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrder) {
        log.info("Receive new Order {}", newOrder);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems())
                .toArray(new Coffee[]{});
        return orderService.createOrder(newOrder.getCustomer(), coffeeList);
    }

    // 以下为本次示例

    /**
     * knowledge point:  ModelAttribute注解，可以表示把这个方法的返回值加入到ModelMap中
     * ModelMap是Controller返回给DispatcherServlet的Model
     */
    @ModelAttribute
    public List<Coffee> coffeeList() {
        return coffeeService.getAllCoffee();
    }

    @GetMapping(path = "/")
    public ModelAndView showCreateForm() {
        /** knowledge point:  这里会返回一个ModelAndView对象，这里只绑定了View的name，
         * 之后ViewResolver就会根据这个name来找到对应的View对象。
         * 另外，因为这个项目配置了参数：前缀和后缀，所以View Name是会加上前缀和后缀一起组成一个完整的视图名的*/
        return new ModelAndView("create-order-form");
    }

    /**
     * knowledge point:
     * 1.APPLICATION_FORM_URLENCODED_VALUE 意思是前端传过来的类型是表单
     * 目前来看前端传过来的应该分了表单、json、文件还有普通的param、pathVariable那种
     */
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createOrder(@Valid NewOrderRequest newOrder,
                              BindingResult result, ModelMap map) {
        if (result.hasErrors()) {
            log.warn("Binding Result: {}", result);
            // DONE_Joly:这里会自动把ModelMap给传递给视图解析器吗--->
            // 应该会，这里方法签名中把ModelMap给传进来了
            map.addAttribute("message", result.toString());
            return "create-order-form";
        }

        log.info("Receive new Order {}", newOrder);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems())
                .toArray(new Coffee[]{});
        CoffeeOrder order = orderService.createOrder(newOrder.getCustomer(), coffeeList);
        /** knowledge point:
         * 302 重定向
         * */
        return "redirect:/order/" + order.getId();
    }
}
