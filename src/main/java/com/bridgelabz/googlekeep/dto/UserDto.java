package com.bridgelabz.googlekeep.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserDto {
	@NotEmpty(message = " user name should not be empty")
	private String name;
    @NotEmpty(message = "user addres should not be empty" )
	private String address;
    @Pattern(regexp = "\\d{10}",message = "mobile number should be 10 digit")
	private String mobile_no;
    @Pattern(regexp = "^(?=.*\\d).{4,8}$",message = "invalid password")
	private String password;
    @Pattern(regexp = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$", message = "Invalid email id")
	private String email;		
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
