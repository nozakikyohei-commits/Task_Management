package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskController {
	
	@GetMapping("/view-tasks")
	public String viewTasks() {
		return "view-tasks";
	}

}
