package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.AppConst;
import com.example.demo.form.RegistUserForm;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	public final UserService userService;
	
	@GetMapping(AppConst.Url.CREATE_USER)
	public String view(Model model) {
		model.addAttribute("form", new RegistUserForm());	//html側でユーザー登録フォームを"form"という名前で使えるようにセット
		return AppConst.View.CREATE_USER;
	}
	
	@PostMapping(AppConst.Url.CREATE_USER)
	public String create(@Valid @ModelAttribute("form") RegistUserForm form, BindingResult result, 
													RedirectAttributes redirectAttributes, Model model) {
		
		//System.out.println("受信したメールアドレス: [" + form.getMailAddress() + "]");
		
		if (userService.checkMailAddress(form.getMailAddress())) {
			//メールアドレスが重複していた場合、html側でフィールド名がmailAddressのものに対してエラーメッセージをセット
			result.rejectValue("mailAddress", "E0008", "{E0008}");	//①フィールド名, ②メッセージコード, ③メッセージ内容
		}
		
		if (result.hasErrors()) {
			//フォワード処理：リクエストを飛ばすのではなく、create-user.htmlというテンプレートを使って画面を作りなおす
			//リクエストはそのままに画面を作り直すだけであり、modelの中身は変わらないので元の入力値は表示されたままになる
			return AppConst.View.CREATE_USER;
		}
		
		userService.create(form);
		
		redirectAttributes.addFlashAttribute("mailAddress", form.getMailAddress());	//リダイレクト先に入力値を渡すための処理
		
		//リダイレクト処理：下記のURLにGETリクエストを送る
		return "redirect:" + AppConst.Url.LOGIN;
		
	}

}
