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
	private LocalDate completedAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	private User user;

}
