package com.example.demo.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditTaskForm {
	
	@NotBlank(message = "{E0010}")	//バリデーションをアノテーションで設定
	@Size(max = 30, message = "{E0013}")
	private String name;
	
	@Size(max = 1000, message = "{E0014}")
	private String content;
	
	@NotNull(message = "{E0011}")
	private int importance;
	
	@NotNull(message = "{E0012}")
	//HTML5の仕様により、type="date"を選択すれば必ずこのフォーマットで送信されるため、今回はこのコードが無くても問題なく動く
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate deadline;
	
	@PastOrPresent(message = "{E0015}")
	private LocalDate completedDate;

}
