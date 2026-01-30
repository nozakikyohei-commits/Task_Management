package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.authentication.CustomUserDetails;
import com.example.demo.constant.AppConst;
import com.example.demo.entity.User;
import com.example.demo.form.EditUserForm;
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
		redirectAttributes.addFlashAttribute("successMessage", "アカウント登録が完了しました。ログインしてください。");
		
		//リダイレクト処理：下記のURLにGETリクエストを送る
		return "redirect:" + AppConst.Url.LOGIN;
		
	}
	
	@GetMapping(AppConst.Url.VIEW_ALL_USERS)
	public String viewAllUsers(@RequestParam(name = "sort", defaultValue = "userId") String sort,
							   @RequestParam(name = "order", defaultValue = "asc") String order,
							   Model model) {
		
		model.addAttribute("users", userService.getAllUsers(sort, order));
		model.addAttribute("currentSort", sort);
		model.addAttribute("currentOrder", order);
		
		return AppConst.View.VIEW_ALL_USERS;
	}
	
	@PostMapping(AppConst.Url.VIEW_ALL_USERS + "/{userId}/delete")
	public String deleteUser(@PathVariable int userId, @AuthenticationPrincipal CustomUserDetails userDetails, 
							 RedirectAttributes redirectAttributes, Model model) {
		
		User loginUser = userDetails.getUser();
		
		//URLを直接入力された場合でも管理者以外はエラー画面を表示する
		if(loginUser.getRole() != AppConst.UserRole.ADMIN) {
			return "error/403";
		}
		
		//URLを直接入力された場合でもログインユーザーは削除できないようにする
		if (userId == loginUser.getUserId()) {
	        redirectAttributes.addFlashAttribute("errorMessage", "自分自身のアカウントは削除できません。");
	        return "redirect:" + AppConst.Url.VIEW_ALL_USERS;
	    }
		
		userService.delete(userId);
		
		return "redirect:" + AppConst.Url.VIEW_ALL_USERS;
	}
	
	@GetMapping(AppConst.Url.EDIT_USER + "/{userId}")
	public String viewEditUser(@PathVariable("userId") int targetUserId, @AuthenticationPrincipal CustomUserDetails userDetails,
            				   @ModelAttribute("form") EditUserForm form, Model model) {
		
		User loginUser = userDetails.getUser();
	    int loginUserId = loginUser.getUserId();
	    int loginUserRole = loginUser.getRole();

	    boolean isAdmin = (loginUserRole == AppConst.UserRole.ADMIN);
	    boolean isMyself = (targetUserId == loginUserId);

	    //「管理者ではない」かつ「自分自身でもない」場合は権限エラーとして返す
	    if (!isAdmin && !isMyself) {
	        return "error/403";
	    }
	    
	    User targetUser = userService.getByUserId(targetUserId);
	    
	    form.setName(targetUser.getName());
	    form.setMailAddress(targetUser.getMailAddress());
	    
	    model.addAttribute("targetUserId", targetUserId);
	    
	    return AppConst.View.EDIT_USER;
	}
	
	@PostMapping(AppConst.Url.EDIT_USER + "/{targetUserId}")
	public String updateUser(@PathVariable("targetUserId") int targetUserId, @AuthenticationPrincipal CustomUserDetails userDetails,
	                         @Valid @ModelAttribute("form") EditUserForm form, BindingResult result, Model model) {

	    User loginUser = userDetails.getUser();
	    int loginUserId = loginUser.getUserId();
	    int loginUserRole = loginUser.getRole();

	    boolean isAdmin = (loginUserRole == AppConst.UserRole.ADMIN);
	    boolean isMyself = (targetUserId == loginUserId);

	    //「管理者ではない」かつ「自分自身でもない」場合は権限エラーとして返す
	    if (!isAdmin && !isMyself) {
	        return "error/403";
	    }
	    
	    if (userService.checkMailAddress(form.getMailAddress())) {
			//メールアドレスが重複していた場合、html側でフィールド名がmailAddressのものに対してエラーメッセージをセット
			result.rejectValue("mailAddress", "E0008", "{E0008}");	//①フィールド名, ②メッセージコード, ③メッセージ内容
		}

	    if (result.hasErrors()) {
			//フォワード処理：リクエストを飛ばすのではなく、create-user.htmlというテンプレートを使って画面を作りなおす
			//リクエストはそのままに画面を作り直すだけであり、modelの中身は変わらないので元の入力値は表示されたままになる
			return AppConst.View.EDIT_USER;
		}
	    
	    userService.update(form, targetUserId);

	    //管理者ならユーザー管理画面へ、一般ならタスク一覧へ戻す
	    if (isAdmin) {
	        return "redirect:" + AppConst.Url.VIEW_ALL_USERS;
	    } else {
	        return "redirect:" + AppConst.Url.VIEW_TASKS;
	    }
	}

}
