package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {
	
	private int userId;
	private String name;
	private String mailAddress;
	private String password;
	private int role;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
