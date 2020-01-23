package com.bridgelabz.googlekeep.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.googlekeep.CustomException.CustomException;
import com.bridgelabz.googlekeep.dto.LoginDto;
import com.bridgelabz.googlekeep.dto.ResetPasswordDto;
import com.bridgelabz.googlekeep.dto.UserDto;
import com.bridgelabz.googlekeep.model.User;
import com.bridgelabz.googlekeep.repository.UserRepository;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.utility.JwtUtil;
import com.bridgelabz.googlekeep.utility.MailUtility;
import com.bridgelabz.googlekeep.utility.Message;

@Service
public class UserServiceImp implements IUserService {

	JwtUtil jwtUtil = new JwtUtil();
	ModelMapper mappper = new ModelMapper();
	@Autowired
	MailUtility mailUtility;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * @purpose : To get list users
	 * @return : List<User>
	 */
	@Override
	public Response getUsers() {
		List<Object> list = new ArrayList<Object>();
		userRepository.findAll().forEach(list::add);
		return new Response(Message.STATUS200, Message.ALL_USERS, list);
	}

	/**
	 * @purpose : To add user
	 * @param userDto : store credential data
	 */
	@Override
	public Response addUser(UserDto userDto) {
		System.out.println(userDto);
		User user = mappper.map(userDto, User.class);
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		System.out.println("encoded");
		User checkuser = userRepository.findByEmail(userDto.getEmail());
		if (checkuser == null) {
			userRepository.save(user);
			return new Response(Message.STATUS200, Message.USER_ADDED, null);
		}
		return new Response(Message.STATUS200, Message.USER_EXIST, null);
	}

	/**
	 * @purpose : remove data
	 * @param id :
	 * @return
	 */
	@Override
	public Response removeUser(String token, int id) {
	     isUser(token);
			User user = userRepository.findById(id).orElseThrow(()-> new CustomException.UserNotExistException("user Not Exist"));
			if (jwtUtil.checkUserById(user)) {
				userRepository.deleteById(id);
				return new Response(Message.STATUS200, Message.USER_REMOVE, null);
			}else return new Response(Message.STATUS200, Message.USER_NOT_FOUND, null);
			
		
					
	}
	

	/**
	 * purpose :search user in repository by id
	 * 
	 * @param id : store int type data
	 * 
	 * 
	 */
	@Override
	public boolean isVerify(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			String token = jwtUtil.generateToken(email);
			String recieveremail = jwtUtil.extractUsername(token);
			String subject = "Verification token";
			String mailsender = "forgotbridge70@gmail.com";
			String text = "token=" + token;
			System.out.println("mail sending......in....progressssssssssss");
			mailUtility.accountVerification(mailsender, recieveremail, subject, text);
			return true;
		} else
			return false;
	}

	/**
	 * @purpose : login verification
	 * @param logindto : store credential data
	 * @return : string
	 */
	@Override
	public Response loginVerification(LoginDto LoginDto) // dto stands for data transfer object
	{
		User user = userRepository.findByEmail(LoginDto.getEmail());
		if (user != null) {
			System.out.println(user.getPassword());
			System.out.println(passwordEncoder.encode(LoginDto.getPassword()));

			if (passwordEncoder.matches(LoginDto.getPassword(), user.getPassword())) {
				String token = jwtUtil.generateToken(user.getEmail());
				String subject = "login token";
				String recieveremail = user.getEmail();
				String mailsender = "forgotbridge70@gmail.com";
				String text = "token=" + token;
				System.out.println("mail sending......in....progressssssssssss");
				mailUtility.accountVerification(mailsender, recieveremail, subject, text);
				return new Response(Message.STATUS200, Message.LOGIN_SUCCESS, null);
			} else
				return new Response(Message.STATUS200, Message.INVALID_PASSWORD, null);
		}
		return new Response(Message.STATUS200, Message.INVALID, null);
	}


	/**
	 * @purpose : To update database
	 * @param token         : store string type data
	 * @param userUpdateDto : store credential data
	 * @return : string
	 */
	@Override
	public Response update(String token, UserDto userDto) {
		User user = isUser(token);
		if (user != null) {
			if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
				user.setName(userDto.getName());
				user.setAddress(userDto.getAddress());
				user.setMobile_no(userDto.getMobile_no());
				userRepository.save(user);
				return new Response(Message.STATUS200, Message.USER_UPDATE, null);
			}
			return new Response(Message.STATUS200, Message.INVALID_PASSWORD, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_FOUND, null);
	}

	/**
	 * @purpose : To change password
	 * @param token             : store string type data
	 * @param forgetPassworddto : store credential data
	 * @return : string
	 */
	@Override
	public Response resetPassword(String token, ResetPasswordDto forgetPassworddto) {
		User user = isUser(token);
		if (forgetPassworddto.getPassword().equals(forgetPassworddto.getConfirmPassword())) {
			user.setPassword(passwordEncoder.encode(forgetPassworddto.getPassword()));
			userRepository.save(user);
			return new Response(Message.STATUS200, Message.PASSWORD_UDATED, null);
		}
		return new Response(Message.STATUS200, Message.INVALID, null);
	}

	/**
	 * @purpose : to verify email
	 * @param emaildto : store credential data
	 * @return : string
	 */
	@Override
	public User isUser(String token) {
		String email = jwtUtil.validateToken(token);
		return userRepository.findByEmail(email);
	}

	@Override
	public Response getUser(String token) {
		User user = isUser(token);
		return new Response(Message.STATUS200, Message.USER_DATA, user);
	}

	@Override
	public Response forgetPasssword(String email) {
		boolean status = isVerify(email);
		if (status) {
			return new Response(Message.STATUS200, Message.TOKEN_GENRATED, null);
		}
		return new Response(Message.STATUS200, Message.INVALID_EMAILID, null);
	}

}