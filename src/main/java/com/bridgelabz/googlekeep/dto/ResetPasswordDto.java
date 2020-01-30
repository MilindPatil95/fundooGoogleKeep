package com.bridgelabz.googlekeep.dto;

import javax.validation.constraints.Pattern;

public class ResetPasswordDto {

	
    @Pattern(regexp = "\\d{10}")
	private String password;                 // Password must be between 4 and 8 digits long and include at least one numeric digit.
    @Pattern(regexp = "\\d{10}")     
    private String confirmPassword;


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
