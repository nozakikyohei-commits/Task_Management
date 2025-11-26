package com.example.demo.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class User {
	
	private int userId;
	private String name;
	private String mailAddress;
	private String password;
	private int role;
	private Timestamp createdAt;
	private Timestamp updatedAt;

}
