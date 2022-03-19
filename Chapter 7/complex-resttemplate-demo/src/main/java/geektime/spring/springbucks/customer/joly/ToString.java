package geektime.spring.springbucks.customer.joly;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author jrl
 * @date Create in 14:26 2022/3/19
 */
public class ToString {
    public static void main(String[] args) {
        Collection a = new ArrayList();
        a.add("a");
        a.add(1);
        // 对于引用类型，会调研ToString方法
        String s = String.valueOf(a);
        System.out.println(s);
        System.out.println(a);
        /** knowledge point:
         * for(;;) 就和 while（true）是等价的
         * */
        while(true){
            if (1==1)
                return;
        }
    }
}
