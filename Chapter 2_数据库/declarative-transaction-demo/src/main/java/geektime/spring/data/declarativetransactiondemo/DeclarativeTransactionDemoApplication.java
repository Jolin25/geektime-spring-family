package geektime.spring.data.declarativetransactiondemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(mode = AdviceMode.PROXY)
@Slf4j
public class DeclarativeTransactionDemoApplication implements CommandLineRunner {
    @Autowired
    private FooService fooService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DeclarativeTransactionDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fooService.insertRecord();
        log.info("AAA {}",
                jdbcTemplate
                        .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='AAA'", Long.class));
        try {
            fooService.insertThenRollback();
        } catch (Exception e) {
            log.info("BBB {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='BBB'", Long.class));
        }

        try {
            // DONE_Joly:这个事务为什么被提交了？
            //  ---> 因为第一个方法没有事务.
            fooService.invokeInsertThenRollback();
        } catch (Exception e) {
            log.info("BBB {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='BBB'", Long.class));
        }

        // 	测试传播行为:NESTED
        try {
            fooService.requiredNested();
            log.info("NESTED {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR LIKE '%NESTED%'", Long.class));
        } catch (Exception e) {
            log.info("EXCEPTION NESTED {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR LIKE '%NESTED%'", Long.class));

        }
        // 	测试传播行为:REQUIRED
        try {
            fooService.required();
            log.info("REQUIRED {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR LIKE '%REQUIRED%'", Long.class));
        } catch (Exception e) {
            log.info("EXCEPTION REQUIRED {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR LIKE '%REQUIRED%'", Long.class));

        }
        // 	测试传播行为:REQUIRED_NEW
        try {
            fooService.requiredNew();
            log.info("REQUIRED_NEW {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR LIKE '%REQUIRED_NEW%'", Long.class));

        } catch (Exception e) {
            log.info("EXCEPTION REQUIRED_NEW {}",
                    jdbcTemplate
                            .queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR LIKE '%REQUIRED_NEW%'", Long.class));


        }
    }
}

