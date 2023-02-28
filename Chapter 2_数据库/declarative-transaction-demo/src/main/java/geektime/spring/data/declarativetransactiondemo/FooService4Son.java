package geektime.spring.data.declarativetransactiondemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jrl
 * @date Create in 23:14 2023/2/23
 */
@Service
public class FooService4Son {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiredNewSon() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('REQUIRED_NEW_SON')");
        // DONE_Joly:只有子事务回滚的情况我模拟不出来，为什么
        //  ---> 模拟不出来的时候，这个方法写在了 FooServiceImpl 类中，和调用方法在同一个类中
        //  Spring 采用的代理模式是按照类的级别来代理的，所以相当于是一个事务。就没有所谓的两个事务。
        int a = 1 / 0;
    }

    @Transactional
    public void requiredSon() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('REQUIRED_SON')");
        int a = 1 / 0;
    }

    @Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT)
    public void requiredNestedSon() {
        // try{
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('NESTED_SON')");
        // int a = 1 / 0;
        // }
        // catch (Exception e){
        //     System.out.println("NESTED_SON 发生异常");
        // }

    }
}
