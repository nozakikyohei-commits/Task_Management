package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Memo;
import com.example.demo.mapper.MemoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {
	
	private final MemoMapper memoMapper;
	
	public Memo getByUserId(int userId) {
		return memoMapper.getByUserId(userId);
	}
	
	@Transactional(readOnly = false)
	public void update(String content, int memoId, int userId) {
		memoMapper.update(content, memoId, userId);
	}

}
