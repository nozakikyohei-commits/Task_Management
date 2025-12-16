package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.authentication.CustomUserDetails;
import com.example.demo.constant.AppConst;
import com.example.demo.entity.Memo;
import com.example.demo.entity.User;
import com.example.demo.form.EditMemoForm;
import com.example.demo.form.EditTaskForm;
import com.example.demo.service.MemoService;
import com.example.demo.service.TaskService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TaskController {
	
	public final TaskService taskService;
	
	public final MemoService memoService;
	
	/*
	 * タスク表示画面の表示
	 */
	@GetMapping(AppConst.Url.VIEW_TASKS)
	//@AuthenticationPrincipalアノテーションにより、ログイン時にSpringSecurityがCustomUserDetailsに保存したユーザー情報を取り出す
	public String viewTasks(@AuthenticationPrincipal CustomUserDetails userDetails,
							@RequestParam(name = "tab", defaultValue = "incomplete") String tab,
							@RequestParam(name = "sort", defaultValue = "deadline") String sort,
							@RequestParam(name = "order", defaultValue = "asc") String order,
							Model model) {
		
		User loginUser = userDetails.getUser();
		int userId = loginUser.getUserId();
		
		if ("completed".equals(tab)) {
	        //完了済みを取得するサービスメソッドを呼ぶ
	        model.addAttribute("tasks", taskService.getCompletedTasksByUserId(userId, sort, order));
	    } else {
	        //未完了（期限切れ含む）を取得するサービスメソッドを呼ぶ
	        taskService.updateStatusToExpired(userId); //更新処理はこっちだけでOK（「期限切れ」への変更対象は「未完了」のもののみであるため）
	        model.addAttribute("tasks", taskService.getIncompleteTasksByUserId(userId, sort, order));
	    }
		
		model.addAttribute("user", loginUser);
		model.addAttribute("currentTab", tab);	//tabによって表示内容を変更するために渡す
		model.addAttribute("currentSort", sort);
		model.addAttribute("currentOrder", order);
		
		return AppConst.View.VIEW_TASKS;
	}
	
	/*
	 * 「完了」ボタン押下によるステータス変更
	 */
	@PostMapping(AppConst.Url.VIEW_TASKS + "/{taskId}/complete")
	public String updateTaskStatusToCompleted(@PathVariable int taskId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		taskService.updateStatusToCompleted(taskId, userDetails.getUser().getUserId());
		
		return "redirect:" + AppConst.Url.VIEW_TASKS + "?tab=completed";
	}
	
	/*
	 * 「未完了に戻す」ボタンによるステータス変更
	 */
	@PostMapping(AppConst.Url.VIEW_TASKS + "/{taskId}/incomplete")
	public String updateTaskStatusToIncompleted(@PathVariable int taskId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		taskService.updateStatusToIncompleted(taskId, userDetails.getUser().getUserId());
		
		return "redirect:" + AppConst.Url.VIEW_TASKS;
	}
	
	/*
	 * カレンダー表示画面の表示
	 */
	@GetMapping(AppConst.Url.VIEW_TASKS_CALENDAR)
	public String viewCalendar(@AuthenticationPrincipal CustomUserDetails userDetails,
						Model model) {
		
		User loginUser = userDetails.getUser();
		
		taskService.updateStatusToExpired(loginUser.getUserId());
		
		model.addAttribute("tasks", taskService.getTasksForCalendar(userDetails.getUser().getUserId()));
		model.addAttribute("user", loginUser);
		
		return AppConst.View.VIEW_TASKS_CALENDAR;
	}
	
	/*
	 * メモ表示画面の表示
	 */
	@GetMapping(AppConst.Url.VIEW_TASKS_MEMOS)
	public String viewMemo(@AuthenticationPrincipal CustomUserDetails userDetails,
							EditMemoForm form,
							Model model) {
		
		User loginUser = userDetails.getUser();
		Memo memo = memoService.getByUserId(loginUser.getUserId());
		form.setContent(memo.getContent());
		
		model.addAttribute("user", loginUser);
		model.addAttribute("memo", memo);
		model.addAttribute("form", form);
		
		return AppConst.View.VIEW_TASKS_MEMOS;
	}
	
	/* 
	 * メモ更新
	 * [キー（htmlのname属性）：content、値：卵、牛乳、小麦粉]というようなデータが送られてくる
	 * 引数に設定してあるform（今回はEditMemoForm）内のフィールド名と同じキー名をもつデータ（今回はcontent）を探し、自動でform.set○○を行ってくれる
	 */
	@PostMapping(AppConst.Url.VIEW_TASKS_MEMOS + "/{memoId}")
	public String updateMemo(@PathVariable int memoId, @AuthenticationPrincipal CustomUserDetails userDetails,
								EditMemoForm form, Model model) {
		
		memoService.update(form.getContent(), memoId, userDetails.getUser().getUserId());
		
		return "redirect:" + AppConst.Url.VIEW_TASKS_MEMOS;
	}
	
	/*
	 * タスク編集画面の表示
	 */
	@GetMapping(AppConst.Url.EDIT_TASK + "/{taskId}")
	public String viewEditTasks(@PathVariable int taskId, @AuthenticationPrincipal CustomUserDetails userDetails,
									@ModelAttribute("form") EditTaskForm form, Model model) {
		
		taskService.getsTaskById(taskId);
		
		model.addAttribute("task", taskService.getsTaskById(taskId));
		
		return AppConst.View.EDIT_TASK;
	}

}
