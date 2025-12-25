package com.example.demo.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.constant.AppConst;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTaskForm {
	
	private int taskId;
	
	private String userName;
	
	private int userId;
	
	private String taskName;
	
	private int importance = AppConst.TaskImportance.UNSELECTED;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate deadline;
	
	private int status = AppConst.TaskStatus.UNSELECTED;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate completedDate;

}
