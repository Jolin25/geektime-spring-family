package top.joly.spring.bean.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.joly.spring.bean.bean.SingletonBean;

/**
 * 测试bean的作用域
 *
 * @author jrl
 * @date Create in 8:18 2022/3/22
 */
@RestController
@RequestMapping("/scope")
public class BeanScope {

    @Autowired
    public SingletonBean singletonBean;

    /**
     * /ˈsɪŋɡltən/
     *
     * @param
     * @return
     * @date 2022/3/22
     */
    @GetMapping("/singleton")
    public void singleton() {
        System.out.println(singletonBean);
    }
}
