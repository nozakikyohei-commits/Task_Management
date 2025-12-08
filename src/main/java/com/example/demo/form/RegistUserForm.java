package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistUserForm {
	
	@NotBlank(message = "{E0004}")
	@Size(max = 20, message = "{E0005}")
	private String name;
	
	@NotBlank(message = "{E0001}")
	@Pattern(regexp = "^[ -~]{,255}$", message = "{E0006}")
	private String mailAddress;
	
	@NotBlank(message = "{E0002}")
	private String password;
	
	

}
