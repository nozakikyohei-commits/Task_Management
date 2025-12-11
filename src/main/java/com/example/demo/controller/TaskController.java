package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.authentication.CustomUserDetails;
import com.example.demo.constant.AppConst;
import com.example.demo.entity.User;
import com.example.demo.service.TaskService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TaskController {
	
	public final TaskService taskService;
	
	@GetMapping(AppConst.Url.VIEW_TASKS)
	//@AuthenticationPrincipalアノテーションにより、ログイン時にSpringSecurityがCustomUserDetailsに保存したユーザー情報を取り出す
	public String viewTasks(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		
		User loginUser = userDetails.getUser();
		int userId = loginUser.getUserId();
		
		taskService.updateStatusToExpired(userId);
		
		model.addAttribute("incompletedTasks", taskService.getIncompleteTasksByUserId(userId));
		model.addAttribute("tasks", taskService.getByUserId(userId));
		model.addAttribute("user", loginUser);
		
		return AppConst.View.VIEW_TASKS;
	}
	
	@PostMapping(AppConst.Url.VIEW_TASKS + "/{taskId}/complete")
	public String updateTaskStatus(@PathVariable int taskId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		taskService.updateStatusToCompleted(taskId, userDetails.getUser().getUserId());
		
		return "redirect:" + AppConst.Url.VIEW_TASKS;
	}

}
