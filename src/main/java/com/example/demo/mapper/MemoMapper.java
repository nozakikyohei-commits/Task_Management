package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Memo;

@Mapper
public interface MemoMapper {
	
	Memo getByUserId(int userId);
	
	void create(int userId);
	
	void update(@Param("content") String content, @Param("memoId") int memoId, @Param("userId") int userId);

}
