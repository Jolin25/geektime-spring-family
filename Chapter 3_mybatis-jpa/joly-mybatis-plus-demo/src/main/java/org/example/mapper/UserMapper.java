package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.SysUser;

import java.util.List;

/**
 * @author jrl
 * @date Create in 18:19 2023/3/16
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
	List<SysUser> selectByXML1();


}
