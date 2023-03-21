package org.example.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author jrl
 * @date Create in 12:42 2023/3/19
 */
@Data
public class Page<T> {
	// org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 11
	// 看样子是没有二级自动封装功能的
	private List<T> content;

	public Page(List<T> content) {
		this.content = content;
	}

	public Page() {
	}
}
