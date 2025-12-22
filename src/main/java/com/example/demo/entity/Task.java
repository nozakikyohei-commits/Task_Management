package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Task {
	
	private int taskId;
	private int userId;
	private String name;
	private String content;
	private int importance;
	private LocalDate deadline;
	private int status;
	private LocalDate completedDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	//タスクから見たユーザーとの関係は1対1であるため、リストではなく単体のユーザーを格納できるよう設定
	private User user;

}
