package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.form.RegistUserForm;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserMapper userMapper;
	
	//フォームに入力されたメールアドレスが既に使用されたものであるかを確認するメソッド
	public boolean checkMailAddress(String mailAddress) {
		
		User user = userMapper.findByMailAddress(mailAddress);
		
		return user != null;
	}
	
	//フォームに入力された「名前」「メールアドレス」「パスワード」を使用して新規ユーザーをDB上に作成するメソッド
	public int create(RegistUserForm form) {
		
		User user = new User();
		
		user.setName(form.getName());
		user.setMailAddress(form.getMailAddress());
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(form.getPassword()));
		
		return userMapper.create(user);
	}

}
