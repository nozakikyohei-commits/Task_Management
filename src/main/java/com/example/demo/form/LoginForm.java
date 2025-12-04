package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
	
	@NotBlank(message = "{E0001}")
	private String mailAddress;
	
	@NotBlank(message= "{E0002}")
	private String password;

}
