package geektime.spring.data.simplejdbcdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Repository
public class FooDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;

    public void insertData() {
        Arrays.asList("b", "c").forEach(bar -> {
            jdbcTemplate.update("INSERT INTO FOO (BAR) VALUES (?)", bar);
        });

        HashMap<String, String> row = new HashMap<>();
        row.put("BAR", "d");
        // DONE_Joly: 这返回的应该是主键 ---> 对，就是主键
        // JDBCTemplate：可以执行用户写好的数据库操作语句或者调用数据库操作语句
        Number id = simpleJdbcInsert.executeAndReturnKey(row);
        log.info("ID of d: {}", id.longValue());
    }

    public void listData() {
        // JdbcTemplate:可以把数据库对象转变成基本数据类型或对象
        log.info("Count: {}",
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO", Long.class));
        // JDBCTemplate：可以执行用户写好的数据库操作语句或者调用数据库操作语句
        List<String> list = jdbcTemplate.queryForList("SELECT BAR FROM FOO", String.class);
        list.forEach(s -> log.info("Bar: {}", s));
        log.info("=========开始执行错误的sql===========");
        // spring dao 会把异常进行统一的封装，并且我们还可以搞自定义异常
        List<Foo> fooList = jdbcTemplate.query("SELECT * FROM FOO1", new RowMapper<Foo>() {
            @Override
            public Foo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Foo.builder()
                        //把sql得到的数据的第一个属性转为Long
                        .id(rs.getLong(1))
                        //把sql得到的数据的属性名为BAR的转为String
                        .bar(rs.getString("BAR"))
                        .build();
            }
        });
        log.info("=========执行完成错误的sql===========");
        log.info("==========Foo List========== ");
        fooList.forEach(f -> log.info("Foo: {}", f));
    }

    public void testCustomException() {
        log.info("===========执行自定义异常sql==========");
        Arrays.asList("bb", "cc").forEach(bar -> {
            jdbcTemplate.update("INSERT INTO FOO (ID,BAR) VALUES (?,?)", 1,bar);
        });
    }
}
