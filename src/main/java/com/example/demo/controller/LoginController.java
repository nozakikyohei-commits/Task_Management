package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.form.LoginForm;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@GetMapping
	public String view(Model model) {
		model.addAttribute("form", new LoginForm());
		return "login";
	}

}
