package com.example.demo.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.authentication.CustomUserDetails;
import com.example.demo.constant.AppConst;
import com.example.demo.entity.Memo;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.form.CreateTaskForm;
import com.example.demo.form.EditMemoForm;
import com.example.demo.form.EditTaskForm;
import com.example.demo.service.MemoService;
import com.example.demo.service.TaskService;

import jakarta.validation.Valid;
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
		//ビュー側で現在のパラメータを参照して次回のリクエストに利用できるように渡しておく
		model.addAttribute("currentTab", tab);
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
	 * タスク作成画面の表示
	 */
	@GetMapping(AppConst.Url.CREATE_TASK)
	public String viewCreateTask(@ModelAttribute("form") CreateTaskForm form, Model model) {
		
		return AppConst.View.CREATE_TASK;
		
	}
	
	/*
	 * タスク作成
	 */
	@PostMapping(AppConst.Url.CREATE_TASK)
	public String createTask(@AuthenticationPrincipal CustomUserDetails userDetails,
								@Valid @ModelAttribute("form") CreateTaskForm form, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			//フォワード処理：リクエストを飛ばすのではなく、create-user.htmlというテンプレートを使って画面を作りなおす
			//リクエストはそのままに画面を作り直すだけであり、modelの中身は変わらないので元の入力値は表示されたままになる
			return AppConst.View.CREATE_TASK;
		}
		
		taskService.create(form, userDetails.getUser().getUserId());
		
		return "redirect:" + AppConst.Url.VIEW_TASKS;
	}
	
	/*
	 * タスク編集画面の表示
	 */
	@GetMapping(AppConst.Url.EDIT_TASK + "/{taskId}")
	public String viewEditTasks(@PathVariable int taskId, @AuthenticationPrincipal CustomUserDetails userDetails,
								EditTaskForm form, Model model) {
		
		Task task = taskService.getsTaskById(taskId);
		User loginUser = userDetails.getUser();
		boolean isSameUser = (task.getUserId() == loginUser.getUserId());
		boolean isAdmin = loginUser.getRole() == AppConst.UserRole.ADMIN;
		
		//どちらでもなければエラー画面を返す
		if (!(isAdmin || isSameUser)) {
	        return "error/403";
	    }
		
		form.setName(task.getName());
		form.setContent(task.getContent());
		form.setImportance(task.getImportance());
		form.setDeadline(task.getDeadline());
		form.setCompletedDate(task.getCompletedDate());
		
		model.addAttribute("task", task);
		model.addAttribute("form", form);
		
		return AppConst.View.EDIT_TASK;
	}
	
	/*
	 * タスク更新
	 */
	@PostMapping(AppConst.Url.EDIT_TASK + "/{taskId}")
	public String updateTask(@PathVariable int taskId, @AuthenticationPrincipal CustomUserDetails userDetails,
								@Valid @ModelAttribute("form") EditTaskForm form, BindingResult result, Model model) {
		
		Task task = taskService.getsTaskById(taskId);
		User loginUser = userDetails.getUser();
		boolean isSameUser = (task.getUserId() == loginUser.getUserId());
		boolean isAdmin = loginUser.getRole() == AppConst.UserRole.ADMIN;
		
		if (result.hasErrors()) {
			model.addAttribute("task", task);
			return AppConst.View.EDIT_TASK;
		}
		
		if(isSameUser) {
			taskService.update(form, taskId, userDetails.getUser().getUserId());
		} else if(isAdmin) {
			taskService.update(form, taskId, task.getUserId());
		} else {
			return "error/403";
		}
		
		if (isAdmin) {
		    return "redirect:" + AppConst.Url.VIEW_ALL_TASKS;
		} else {
		    return "redirect:" + AppConst.Url.VIEW_TASKS;
		}
	}
	
	/*
	 * タスク削除
	 */
	@PostMapping(AppConst.Url.EDIT_TASK + "/{taskId}/delete")
	public String deleteTask(@PathVariable int taskId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		
		Task task = taskService.getsTaskById(taskId);
		User loginUser = userDetails.getUser();
		boolean isSameUser = (task.getUserId() == loginUser.getUserId());
		boolean isAdmin = loginUser.getRole() == AppConst.UserRole.ADMIN;
		
		if(isSameUser) {
			taskService.delete(taskId, loginUser.getUserId());
		} else if(isAdmin) {
			taskService.delete(taskId, task.getUserId());
		} else {
			return "error/403";
		}
		
		return "redirect:" + AppConst.Url.VIEW_TASKS;
	}
	
	/*
	 * 全タスク表示画面の表示
	 */
	@GetMapping(AppConst.Url.VIEW_ALL_TASKS)
	public String viewAllTasks(@AuthenticationPrincipal CustomUserDetails userDetails,
							   @RequestParam(name = "sort", defaultValue = "taskId") String sort,
							   @RequestParam(name = "order", defaultValue = "asc") String order,
							   Model model) {
		
		int userRole = userDetails.getUser().getRole();
		
		if(userRole != AppConst.UserRole.ADMIN) {
			return "error/403";
		}
		
		List<Task> tasks = taskService.getAllTasks(sort, order);
		model.addAttribute("tasks", tasks);
		model.addAttribute("currentSort", sort);
		model.addAttribute("currentOrder", order);
		
		return AppConst.View.VIEW_ALL_TASKS;
	}

}
