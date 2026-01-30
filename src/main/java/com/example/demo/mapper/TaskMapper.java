package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Task;
import com.example.demo.form.SearchTaskForm;

@Mapper
public interface TaskMapper {
	
	Task getsTaskById(int taskId);
	
	List<Task> getTasksForCalendar(
			@Param("userId") int userId,
			@Param("incomplete") int incomplete,
			@Param("expired") int expired
	);
	
	List<Task> getIncompleteTasksByUserId(
			@Param("userId") int userId, 
		    @Param("sort") String sort, 
		    @Param("order") String order,
		    @Param("incomplete") int incomplete,
			@Param("expired") int expired
	);
	
	List<Task> getCompletedTasksByUserId(
			@Param("userId") int userId, 
		    @Param("sort") String sort, 
		    @Param("order") String order,
		    @Param("completed") int completed,
			@Param("expiredCompleted") int expiredCompleted
	);
	
	List<Task> searchTasks(
			@Param("form") SearchTaskForm form,
			@Param("sort") String sort, 
		    @Param("order") String order
	);
	
	void create(Task task);
	
	void update(Task task);
	
	void updateStatusToExpired(
			@Param("userId") int userId,
			@Param("incomplete") int incomplete,
			@Param("expired") int expired
	);
	
	//引数が複数ある場合、SQL側で混乱が生じるリスクがあるため、各引数に名前をつけておく
	void updateStatusToCompleted(
			@Param("taskId") int taskId,
			@Param("userId") int userId,
			@Param("completed") int completed,
			@Param("expiredCompleted") int expiredCompleted
	);
	
	void updateStatusToIncompleted(
			@Param("taskId") int taskId,
			@Param("userId") int userId,
			@Param("incomplete") int incomplete,
			@Param("expired") int expired
	);
	
	void delete(
			@Param("taskId") int taskId,
			@Param("userId") int userId
	);
	
	void deleteTaskWithUser(int userId);

}
