package com.bridgelabz.googlekeep.controller;
import java.io.IOException;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.googlekeep.dto.LoginDto;
import com.bridgelabz.googlekeep.dto.ResetPasswordDto;
import com.bridgelabz.googlekeep.dto.UserDto;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.service.UserServiceImp;
import com.bridgelabz.googlekeep.utility.Message;
@RestController
@RequestMapping("/fundoo")
public class UserController {
	@Autowired
	private UserServiceImp userService;
	@GetMapping("/users")
	public Response users() {
		return userService.getUsers();
	}
	
	@PostMapping("/registration")
	public Response addUser(@Valid @RequestBody UserDto userDto) {
		return userService.addUser(userDto);
	}
	
	@PostMapping("/login")
	public Response loginUser(@Valid @RequestBody LoginDto loginDto ) {
		return userService.login(loginDto);
	}
	
	@GetMapping("/getUserDetail")
	public Response getUserDetail(@RequestHeader int id) {
		return userService.getUser(id);
	}
	
	@PutMapping("/userUpdate")
	public Response updateUser(@Valid @RequestBody UserDto userDto)
	{    
		return  userService.update(userDto);
	}	
	
	@PutMapping("/forgetPassword")
	public  Response forgetPassword(@RequestHeader String email)
	{      return userService.forgetPasssword(email);
	}	
	
	@PutMapping("/resetPassword")
	public Response resetPassword(@RequestHeader ResetPasswordDto resetPasswordDto,@RequestHeader int userid)
	{	  
		  return userService.resetPassword(userid, resetPasswordDto);
	}
//	@GetMapping("removeUser")
//	public Response removeUser(@RequestHeader String token, @RequestHeader int id)
//	{
//		return userService.removeUser(token,id);
//	}
	@PutMapping(value ="/uploadprofile",consumes = "multipart/form-data")
	public Response uploadProfile(@RequestHeader String token, @RequestPart(value = "file")MultipartFile file) throws IOException
	{
		return userService.saveProfile(file,token);
	}
	@GetMapping("/logout")
	public Response logOut(@RequestHeader String token)
	{
		return  userService.logOut(token);
	}
	@GetMapping("/isverifiedToken")
	public Response isverifiedToken(@RequestHeader String token)
	{          
		return userService.isverifiedToken(token);
	}
	
	
	
}