package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {
	
	//登録されたメールアドレスによって特定のユーザーをDB上から探し出すメソッド
	//ログイン認証時、ユーザー登録時のメールアドレス重複チェックに使用
	User findByMailAddress(String mailAddress);
	
	//ユーザー新規登録をするためのメソッド
	int create(User user);
}
