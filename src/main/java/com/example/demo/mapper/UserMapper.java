package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {
	
	User findByMailAddress(String mailAddress);

}
