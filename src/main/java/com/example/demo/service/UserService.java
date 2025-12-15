package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.form.RegistUserForm;
import com.example.demo.mapper.MemoMapper;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	
	private final UserMapper userMapper;
	
	private final MemoMapper memoMapper;
	
	//フォームに入力されたメールアドレスが既に使用されたものであるかを確認するメソッド
	public boolean checkMailAddress(String mailAddress) {
		
		User user = userMapper.findByMailAddress(mailAddress);
		
		return user != null;
	}
	
	@Transactional(readOnly = false)
	//フォームに入力された「名前」「メールアドレス」「パスワード」を使用して新規ユーザーをDB上に作成するメソッド
	public void create(RegistUserForm form) {
		
		User user = new User();
		
		user.setName(form.getName());
		user.setMailAddress(form.getMailAddress());
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(form.getPassword()));
		
		//以下のメソッドでユーザーを作成した際、xml側の記述により作成したユーザーのIDをuserIdにセット
		userMapper.create(user);
		
		//xml側から受け取った値をもとにメモを作成
		memoMapper.create(user.getUserId());
	}

}
