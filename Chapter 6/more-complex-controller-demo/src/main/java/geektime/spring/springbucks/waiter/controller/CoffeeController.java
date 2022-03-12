package geektime.spring.springbucks.waiter.controller;

import geektime.spring.springbucks.waiter.controller.request.NewCoffeeRequest;
import geektime.spring.springbucks.waiter.controller.request.ValidList;
import geektime.spring.springbucks.waiter.controller.request.ValidOne;
import geektime.spring.springbucks.waiter.model.Coffee;
import geektime.spring.springbucks.waiter.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 对前端的传参进行校验、类型转换，以及接受文件
 *
 * @author Joly
 */
@Controller
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    /**
     * 对传入的参数进行校验，并不用spring的错误处理，而是自己处理
     *
     * @param
     * @return
     * @date 2022/3/11
     */
    @PostMapping(path = "/with_result", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffee(@Valid NewCoffeeRequest newCoffee,
                            BindingResult result) {
        if (result.hasErrors()) { // 自己处理校验错误
            // 这里先简单处理一下，后续讲到异常处理时会改
            log.warn("Binding Errors: {}", result);
            return null;
        }
        return coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
    }

    /**
     * 1.Valid 注解 用于对参数进行校验
     * 会返回400：Bad Request
     * 2.TODO_Joly RequestBody注解可以把前端传回来的数据转换成对象，但是前端如果传的是表单，好像就自动转换成对象了
     *
     * @param
     * @return
     * @date 2022/3/10
     */
    @PostMapping(path = "/without_result", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffeeWithoutBindingResult(@Valid NewCoffeeRequest newCoffee) {
        return coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
    }

    /**
     * 1.Validated 注解和Valid注解可能差别不大---> 确实不大，Validated是Spring对Valid的优化
     * 2.DONE_Joly: 但是为什么要用ValidList去包一层呢？直接用List不行吗？既然都是去校验的List
     * ---> 因为Valid注解校验参数的话只能校验JavaBean类型的，List不是的撒，所以就自己搞个JavaBean
     * 形式的List来包一下，然后依靠Valid注解放在属性上去做级联校验。
     *
     * @param
     * @return
     * @date 2022/3/11
     */
    @PostMapping(path = "/test_valid")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public String testValid(@RequestBody @Valid ValidList<ValidOne> requestValidList, BindingResult br) {
        if (br.hasErrors()) {
            System.out.println(br);
            return "error";
        }
        return String.valueOf(requestValidList.size()) + br.hasErrors();
    }

    /**
     * 1.文件上传演示
     * 2.关于使用了Post请求，而参数列表用RequestParam注解来接收：
     * 这个注解的注释里是这么写的：In Spring MVC, "request parameters" map to query parameters, form data, and parts in multipart requests.
     * 我想你好奇的是这里为什么我不用RequstBody吧？这个注解是把整个请求的Body传给参数，而RequestParam是可以把Body里的对应部分取出来传给参数。
     *
     * @param
     * @return
     * @date 2022/3/10
     */
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public List<Coffee> batchAddCoffee(@RequestParam("file") MultipartFile file) {
        List<Coffee> coffees = new ArrayList<>();
        if (!file.isEmpty()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(file.getInputStream()));
                String str;
                while ((str = reader.readLine()) != null) {
                    String[] arr = StringUtils.split(str, " ");
                    if (arr != null && arr.length == 2) {
                        coffees.add(coffeeService.saveCoffee(arr[0],
                                Money.of(CurrencyUnit.of("CNY"),
                                        NumberUtils.createBigDecimal(arr[1]))));
                    }
                }
            } catch (IOException e) {
                log.error("exception", e);
            } finally {
                // tomcat提供的io连接关闭工具
                IOUtils.closeQuietly(reader);
            }
        }
        return coffees;
    }

    @GetMapping(path = "/", params = "!name")
    @ResponseBody
    public List<Coffee> getAll() {
        return coffeeService.getAllCoffee();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Coffee getById(@PathVariable Long id) {
        Coffee coffee = coffeeService.getCoffee(id);
        return coffee;
    }

    @GetMapping(path = "/", params = "name")
    @ResponseBody
    public Coffee getByName(@RequestParam String name) {
        return coffeeService.getCoffee(name);
    }
}
