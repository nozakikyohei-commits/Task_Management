package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Task;

@Mapper
public interface TaskMapper {
	
	Task getsTaskById(int taskId);
	
	List<Task> getTasksForCalendar(int userId);
	
	List<Task> getIncompleteTasksByUserId(
			@Param("userId") int userId, 
		    @Param("sort") String sort, 
		    @Param("order") String order
	);
	
	List<Task> getCompletedTasksByUserId(
			@Param("userId") int userId, 
		    @Param("sort") String sort, 
		    @Param("order") String order
	);

	
	void updateStatusToExpired(int userId);
	
	//引数が複数ある場合、SQL側で混乱が生じるリスクがあるため、各引数に名前をつけておく
	void updateStatusToCompleted(@Param("taskId") int taskId, @Param("userId") int userId);
	
	void updateStatusToIncompleted(@Param("taskId") int taskId, @Param("userId") int userId);

}
