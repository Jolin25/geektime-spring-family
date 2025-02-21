package geektime.spring.data.mybatis;

import geektime.spring.data.mybatis.mapper.CoffeeMapper;
import geektime.spring.data.mybatis.model.Coffee;
import geektime.spring.data.mybatis.model.CoffeeExample;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Slf4j
@MapperScan("geektime.spring.data.mybatis.mapper")
public class MybatisGeneratorDemoApplication implements ApplicationRunner {
	@Autowired
	private CoffeeMapper coffeeMapper;

	public static void main(String[] args) {
		SpringApplication.run(MybatisGeneratorDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// generateArtifacts();
		playWithArtifacts();
	}

	// knowledge use:使用 Mybatis Generator 生成 xxxExample，xxxMapper,xxxMapper.xml
	// private void generateArtifacts() throws Exception {
	// 	List<String> warnings = new ArrayList<>();
	// 	ConfigurationParser cp = new ConfigurationParser(warnings);
	// 	Configuration config = cp.parseConfiguration(
	// 			this.getClass().getResourceAsStream("/generatorConfig.xml"));
	// 	DefaultShellCallback callback = new DefaultShellCallback(true);
	// 	MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
	// 	myBatisGenerator.generate(null);
	// }
   // knowledge use:使用 MyBatis Generator 生成的代码，来进行操作
	private void playWithArtifacts() {
		// 插入
		Coffee espresso = new Coffee().withName("espresso").withPrice(Money.of(CurrencyUnit.of("CNY"), 20.0)).withCreateTime(new Date()).withUpdateTime(new Date());
		coffeeMapper.insert(espresso);

		// 插入
		Coffee latte = new Coffee().withName("latte").withPrice(Money.of(CurrencyUnit.of("CNY"), 30.0)).withCreateTime(new Date()).withUpdateTime(new Date());
		coffeeMapper.insert(latte);

		// 查找：根据主键
		Coffee s = coffeeMapper.selectByPrimaryKey(1L);
		log.info("Coffee {}", s);

		// 查找：根据名称
		CoffeeExample example = new CoffeeExample();
		example.createCriteria().andNameEqualTo("latte");
		List<Coffee> list = coffeeMapper.selectByExample(example);
		list.forEach(e -> log.info("selectByExample: {}", e));
	}
}

