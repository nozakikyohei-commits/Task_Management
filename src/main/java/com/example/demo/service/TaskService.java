package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Task;
import com.example.demo.form.CreateTaskForm;
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
		
		return taskMapper.getTasksForCalendar(userId);
	}
	
	public List<Task> getIncompleteTasksByUserId(int userId, String sort, String order) {
		
		return taskMapper.getIncompleteTasksByUserId(userId, sort, order);
	}
	
	public List<Task> getCompletedTasksByUserId(int userId, String sort, String order) {
		
		return taskMapper.getCompletedTasksByUserId(userId, sort, order);
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
	public void updateStatusToExpired(int userId) {
		taskMapper.updateStatusToExpired(userId);
	}
	
	@Transactional(readOnly = false)
	public void updateStatusToCompleted(int taskId, int userId) {
		taskMapper.updateStatusToCompleted(taskId, userId);
	}
	@Transactional(readOnly = false)
	public void updateStatusToIncompleted(int taskId, int userId) {
		taskMapper.updateStatusToIncompleted(taskId, userId);
	}

}
