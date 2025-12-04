package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserMapper userMapper;
	

}
