package com.bridgelab.controller;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.bridgelab.dto.ResetPasswordDto;
import com.bridgelab.dto.LoginDto;
import com.bridgelab.dto.UserDto;
import com.bridgelab.response.Response;
import com.bridgelab.service.UserService;
import com.bridgelab.utility.Message;

@RestController
@RequestMapping("/fundoo")
public class UserController {
	@Autowired
	private UserService userService;
	  boolean status=false;
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
	
	@PutMapping("/forgotPassword")
	public  Response forgotPassword(@RequestHeader int id)
	{      status= userService.isVerify(id);
	       if(status)
	       {
	    	   return  new Response(Message.STATUS200, Message.USER_VERIFIED, null);
	       }
	       else return new Response(Message.STATUS200, Message.USER_VERIFIED, null);
	}
	
	@PutMapping("/resetPassword")
	public Response resetPassword(@RequestBody ResetPasswordDto resetPasswordDto,@RequestHeader String token)
	{
		  
		  return userService.resetPassword(token, resetPasswordDto);
	}
	
	
}