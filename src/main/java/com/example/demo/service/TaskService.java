package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.AppConst;
import com.example.demo.entity.Task;
import com.example.demo.form.CreateTaskForm;
import com.example.demo.form.EditTaskForm;
import com.example.demo.form.SearchTaskForm;
import com.example.demo.mapper.TaskMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
	
	private final TaskMapper taskMapper;
	
	public Task getsTaskById(int taskId) {
		return taskMapper.getsTaskById(taskId);
	}
	
	public List<Task> getTasksForCalendar(int userId) {
		
		return taskMapper.getTasksForCalendar(userId, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	public List<Task> getIncompleteTasksByUserId(int userId, String sort, String order) {
		
		return taskMapper.getIncompleteTasksByUserId(userId, sort, order, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	public List<Task> getCompletedTasksByUserId(int userId, String sort, String order) {
		
		return taskMapper.getCompletedTasksByUserId(userId, sort, order, AppConst.TaskStatus.COMPLETED, AppConst.TaskStatus.EXPIRED_COMPLETED);
	}
	
	public List<Task> searchTasks(SearchTaskForm form, String sort, String order) {
		
		return taskMapper.searchTasks(form, sort, order);
	}
	
	@Transactional(readOnly = false)
	public void create(CreateTaskForm form, int userId) {
		
		Task task = new Task();
		
		task.setUserId(userId);
		task.setName(form.getName());
		task.setContent(form.getContent());
		task.setImportance(form.getImportance());
		task.setDeadline(form.getDeadline());
		
		taskMapper.create(task);
	}
	
	@Transactional(readOnly = false)
	public void update(EditTaskForm form, int taskId, int userId) {
		
		Task task = new Task();
		
		task.setTaskId(taskId);
		task.setUserId(userId);
		task.setName(form.getName());
		task.setContent(form.getContent());
		task.setImportance(form.getImportance());
		task.setDeadline(form.getDeadline());
		task.setCompletedDate(form.getCompletedDate());
		
		int newStatus = determineStatus(form.getCompletedDate(), form.getDeadline());
		task.setStatus(newStatus);
		
		taskMapper.update(task);
	}
	
		//タスク更新時に、フォームに入力されている完了日と期限に応じたステータスを返すメソッド
		private int determineStatus(LocalDate completedDate, LocalDate deadline) {
			
			LocalDate today = LocalDate.now();
	
			//完了日が設定されている場合
			if (completedDate != null) {
				//期限が設定されていて、かつ「完了日」が「期限」を過ぎている場合
				if (deadline != null && completedDate.isAfter(deadline)) {
					return AppConst.TaskStatus.EXPIRED_COMPLETED; //期限切れ完了 
				}
				//それ以外は通常の「完了」
				return AppConst.TaskStatus.COMPLETED; //完了
			}
			
			//完了日が設定されていない場合
			else {
				//期限が設定されていて、かつ「期限」が「今日」より前の場合
				if (deadline != null && deadline.isBefore(today)) {
					return AppConst.TaskStatus.EXPIRED; //期限切れ
				}
				//それ以外は「未完了」
				return AppConst.TaskStatus.INCOMPLETE; //未完了
			}
		}
	
	@Transactional(readOnly = false)
	public void updateStatusToExpired(int userId) {
		taskMapper.updateStatusToExpired(userId, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	@Transactional(readOnly = false)
	public void updateStatusToCompleted(int taskId, int userId) {
		taskMapper.updateStatusToCompleted(taskId, userId, AppConst.TaskStatus.COMPLETED, AppConst.TaskStatus.EXPIRED_COMPLETED);
	}
	
	@Transactional(readOnly = false)
	public void updateStatusToIncompleted(int taskId, int userId) {
		taskMapper.updateStatusToIncompleted(taskId, userId, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	@Transactional(readOnly = false)
	public void delete(int taskId, int userId) {
		taskMapper.delete(taskId, userId);
	}

}
