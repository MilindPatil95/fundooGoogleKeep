package com.bridgelabz.googlekeep.dto;

import javax.validation.constraints.Pattern;

public class LoginDto {
	 @Pattern(regexp = "^(?=.*\\d).{4,8}$",message = "password should have total range 8 char  and atleast one  digit")
		private String password;
	    @Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$", message = "invalid email id")
		private String email;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
}
