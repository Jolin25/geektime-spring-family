package top.joly.sb0.boot0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Joly
 */
@SpringBootApplication
public class Boot0Application {
    // TODO_Joly: 这个 args 是什么参数？
    public static void main(String[] args) {
        SpringApplication.run(Boot0Application.class, args);
        /** knowledge point:  数组的神奇语法*/
        // Class<?>[] arr = new Class<?>[]{Boot0Application.class};
        // Integer[] arr1 = new Integer[5];
        // // 这个创建数组的神奇语法哟，真的是给人搞得难受死了，创建的同时进行了初始化，但是指定初始化
        // //和不指定初始化的语法居然有语法上的神奇不同，竟然不是只加个参数
        // Integer[] arr2 = new Integer[]{1, 2, 3};

    }

}
