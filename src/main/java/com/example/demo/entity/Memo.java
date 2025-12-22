package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Memo {
	
	private int memoId;
	private int userId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
