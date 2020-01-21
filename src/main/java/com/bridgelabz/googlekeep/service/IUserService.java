package com.bridgelabz.googlekeep.service;

import com.bridgelabz.googlekeep.dto.LoginDto;
import com.bridgelabz.googlekeep.dto.ResetPasswordDto;
import com.bridgelabz.googlekeep.dto.UserDto;
import com.bridgelabz.googlekeep.model.User;
import com.bridgelabz.googlekeep.response.Response;

public interface IUserService {
	public Response addUser(UserDto userDto);
	public Response getUser(String token);		
	public Response getUsers();	
	public Response removeUser(String token,int id);
	public boolean isVerify(String email);
	public Response loginVerification(LoginDto LoginDto);
	public Response update(String token, UserDto userDto);
	public Response resetPassword(String token, ResetPasswordDto forgetPassworddto);
	public Response forgetPasssword(String email);		
	public User isUser(String token);
	
		
		
		
		
}
