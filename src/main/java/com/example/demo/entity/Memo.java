package com.example.demo.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Memo {
	
	private int memoId;
	private int userId;
	private String content;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	
	private User user;

}
