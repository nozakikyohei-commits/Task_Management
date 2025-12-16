package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.constant.AppConst;
import com.example.demo.form.LoginForm;

@Controller
@RequestMapping(AppConst.Url.LOGIN)
public class LoginController {
	
	@GetMapping
	public String view(@ModelAttribute("mailAddress") String mailAddress,
						@ModelAttribute("form") LoginForm form) {
			
		form.setMailAddress(mailAddress);	//ユーザー新規登録画面で入力したメールアドレスを受け取ってformにセット
		return AppConst.View.LOGIN;
	}

}
