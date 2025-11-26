package com.example.demo.entity;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Task {
	
	private int taskId;
	private int userId;
	private String name;
	private String content;
	private int importance;
	private Date deadline;
	private int status;
	private Timestamp completedAt;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	
	private User user;

}
