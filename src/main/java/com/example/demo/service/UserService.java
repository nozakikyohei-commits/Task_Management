package com.example.demo.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.form.EditUserForm;
import com.example.demo.form.RegistUserForm;
import com.example.demo.mapper.MemoMapper;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	
	private final UserMapper userMapper;
	
	private final MemoMapper memoMapper;
	
	private final TaskMapper taskMapper;
	
	//フォームに入力されたメールアドレスが既に使用されたものであるかを確認するメソッド
	public boolean checkMailAddress(String mailAddress) {
		
		User user = userMapper.findByMailAddress(mailAddress);
		
		return user != null;
	}
	
	public List<User> getAllUsers(String sort, String order) {
		return userMapper.getAllUsers(sort, order);
	}
	
	public User getByUserId(int userId) {
		return userMapper.getByUserId(userId);
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
	
	@Transactional(readOnly = false)
	public void update(EditUserForm form, int userId) {
		
		User user = new User();
		
		user.setUserId(userId);
		user.setName(form.getName());
		user.setMailAddress(form.getMailAddress());
		
		if (form.getPassword() == null || form.getPassword().isEmpty()) {
	        
	        //入力がない場合は現在のパスワードをDBから取得してセットする（ハッシュ化済みのものなので、ここでもう一度ハッシュ化してはならない）
			User targetUser = userMapper.getByUserId(userId);
	        user.setPassword(targetUser.getPassword());
	        
	    } else {
	        
	        //入力がある場は新しくハッシュ化してセットする
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        user.setPassword(encoder.encode(form.getPassword()));
	        
	    }
		
		userMapper.update(user);
	}
	
	@Transactional(readOnly = false)
	public void delete(int userId) {
		userMapper.delete(userId);
		taskMapper.deleteTaskWithUser(userId);
		memoMapper.deleteMemoWithUser(userId);
	}

}
