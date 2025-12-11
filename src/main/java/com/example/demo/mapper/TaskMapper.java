package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Task;

@Mapper
public interface TaskMapper {
	
	List<Task> getByUserId(int userId);
	
	List<Task> getIncompleteTasksByUserId(int userId);
	
	void updateStatusToExpired(int userId);
	
	//引数が複数ある場合、SQL側で混乱が生じるリスクがあるため、各引数に名前をつけておく
	void updateStatusToCompleted(@Param("taskId") int taskId, @Param("userId") int userId);

}
