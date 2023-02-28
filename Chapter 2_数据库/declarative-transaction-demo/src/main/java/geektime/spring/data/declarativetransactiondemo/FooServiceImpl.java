package geektime.spring.data.declarativetransactiondemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * knowledge point:
 * Spring 事务 针对的是代理对象，如果调用同一个类中直接调用方法，事务不生效（可以自己注入自己调用）
 */
@Component
@Slf4j
public class FooServiceImpl implements FooService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService4Son fooService4Son;

    @Override
    @Transactional
    public void insertRecord() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('AAA')");
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('BBB')");
        throw new RollbackException();
    }

    @Override
    public void invokeInsertThenRollback() throws RollbackException {
        // DONE_Joly:为什么没有回滚呢
        //  ---> 因为 在 Spring 中，只要调用的第一个方法没有事务，
        //  那么就算整体没有事务.那就是直接提交.不管这是不是在同一个类中.
        insertThenRollback();
    }

    @Transactional
    @Override
    public void requiredNested() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('NESTED_FATHER')");
        int a = 1 / 0;
        try {
            fooService4Son.requiredNestedSon();
        } catch (Exception e) {
            System.out.println("NESTED_SON 发生异常，被 FATHER 捕获。");
        }
    }

    @Transactional
    @Override
    public void required() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('REQUIRED')");
        // requiredSon();
        try {
            fooService4Son.requiredSon();
        } catch (Exception e) {
            System.out.println("REQUIRED FATHER 捕获 SON 异常。");
        }
        // int a = 1 / 0;
    }

    @Transactional
    @Override
    public void requiredNew() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('REQUIRED_NEW')");
        try {
            fooService4Son.requiredNewSon();
        } catch (Exception e) {

        }
    }

}
