package com.example.demo.form;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditTaskForm {
	
	private String name;
	
	private String content;
	
	private int importance;
	
	private Date deadline;

}
