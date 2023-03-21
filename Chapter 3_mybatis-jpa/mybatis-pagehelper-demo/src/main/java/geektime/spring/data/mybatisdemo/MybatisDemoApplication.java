package geektime.spring.data.mybatisdemo;

import com.github.pagehelper.PageInfo;
import geektime.spring.data.mybatisdemo.mapper.CoffeeMapper;
import geektime.spring.data.mybatisdemo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@MapperScan("geektime.spring.data.mybatisdemo.mapper")
public class MybatisDemoApplication implements ApplicationRunner {
	@Autowired
	private CoffeeMapper coffeeMapper;

	public static void main(String[] args) {
		SpringApplication.run(MybatisDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// knowledge use: 方式一：使用 RowBounds , 逻辑分页

		// 配置里面设置了 pagehelper.offset-as-page-num=true，
		// 所以 offset 是页码

		// 第一页，每页3条记录。（第x页，就是从1开始的，不是0）
		coffeeMapper.findAllWithRowBounds(new RowBounds(1, 3))
				.forEach(c -> log.info("Page(1) Coffee {}", c));
		coffeeMapper.findAllWithRowBounds(new RowBounds(2, 3))
				.forEach(c -> log.info("Page(2) Coffee {}", c));

		log.info("===================");
		// pagesize 是 0 的时候，会把所有的记录都取出来。（这个是配置文件里面设置的）
		coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
				.forEach(c -> log.info("Page(1) Coffee {}", c));

		log.info("===================");

		// knowledge use:方式二：直接标注 pageNum,pageSize 作为方法参数
		coffeeMapper.findAllWithParam(1, 3)
				.forEach(c -> log.info("Page(1) Coffee {}", c));
		List<Coffee> list = coffeeMapper.findAllWithParam(2, 3);
		// PageInfo
		PageInfo page = new PageInfo(list);
		log.info("PageInfo: {}", page);

		// knowledge use:还有其他方式，建议看 PageHelper 官网
	}
}

