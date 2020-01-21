package com.bridgelabz.googlekeep.controller;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelabz.googlekeep.dto.LoginDto;
import com.bridgelabz.googlekeep.dto.ResetPasswordDto;
import com.bridgelabz.googlekeep.dto.UserDto;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.service.UserServiceImp;
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
		return userService.loginVerification(loginDto);
	}
	
	@GetMapping("/getUserDetail")
	public Response getUserDetail(@RequestHeader String token) {
		return userService.getUser(token);
	}
	
	@PutMapping("/userUpdate")
	public Response updateUser(@RequestHeader String token,@Valid @RequestBody UserDto userDto)
	{    
		return  userService.update(token,userDto);
	}	
	
	@PutMapping("/forgetPassword")
	public  Response forgetPassword(@RequestHeader String email)
	{      return userService.forgetPasssword(email);
	}	
	
	@PutMapping("/resetPassword")
	public Response resetPassword(@RequestBody ResetPasswordDto resetPasswordDto,@RequestHeader String token)
	{	  
		  return userService.resetPassword(token, resetPasswordDto);
	}
	@GetMapping("removeUser")
	public Response removeUser(@RequestHeader String token, @RequestHeader int id)
	{
		return userService.removeUser(token,id);
	}
	
	
	
}