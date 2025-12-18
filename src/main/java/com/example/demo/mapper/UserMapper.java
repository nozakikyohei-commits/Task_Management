package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {
	
	//登録されたメールアドレスによって特定のユーザーをDB上から探し出すメソッド
	//ログイン認証時、ユーザー登録時のメールアドレス重複チェックに使用
	User findByMailAddress(String mailAddress);
	
	//ユーザー新規登録をするためのメソッド
	void create(User user);
	
	List<User> getAllUsers(@Param("sort") String sort, @Param("order") String order);
}
