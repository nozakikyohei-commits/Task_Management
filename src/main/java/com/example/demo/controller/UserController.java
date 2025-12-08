package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.form.RegistUserForm;

@Controller
public class UserController {
	
	@GetMapping("/create-user")
	private String view(Model model) {
		model.addAttribute("form", new RegistUserForm());
		return "create-user";
	}

}
