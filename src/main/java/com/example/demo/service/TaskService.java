package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.mapper.TaskMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	
	private final TaskMapper taskMapper;
	
	public List<Task> getByUserId(int userId) {
		
		return taskMapper.getByUserId(userId);
	}
	
	public List<Task> getIncompleteTasksByUserId(int userId) {
		
		return taskMapper.getIncompleteTasksByUserId(userId);
	}
	
	public void updateStatusToExpired(int userId) {
		taskMapper.updateStatusToExpired(userId);
	}
	
	public void updateStatusToCompleted(int taskId, int userId) {
		taskMapper.updateStatusToCompleted(taskId, userId);
	}

}
