package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.constant.AppConst;
import com.example.demo.form.LoginForm;

@Controller
@RequestMapping(AppConst.Url.LOGIN)
public class LoginController {
	
	@GetMapping
	public String view(@ModelAttribute("mailAddress") String mailAddress, Model model) {
		LoginForm form = new LoginForm();
		model.addAttribute("form", form);	//html側でログインフォームを"form"という名前で使えるようにセット
		form.setMailAddress(mailAddress);	//ユーザー新規登録画面で入力したメールアドレスを受け取ってformにセット
		return AppConst.View.LOGIN;
	}

}
