package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.pojo.Card;
import org.example.pojo.Employee;
import org.example.pojo.Page;

import java.util.List;

/**
 * @author jrl
 * @date Create in 12:56 2023/3/15
 */
// knowledge use:应该是给 MyBatis 做拦截用的
@Mapper
public interface CardMapper {
	List<Card> oneToMany();

	List<Card>	oneToManyFather();
}
