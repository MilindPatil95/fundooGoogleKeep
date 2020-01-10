package com.bridgelab.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelab.dto.ResetPasswordDto;
import com.bridgelab.dto.LoginDto;
import com.bridgelab.dto.UserDto;
import com.bridgelab.model.User;
import com.bridgelab.repository.UserRepository;
import com.bridgelab.response.Response;
import com.bridgelab.utility.JwtUtil;
import com.bridgelab.utility.MailUtility;
import com.bridgelab.utility.Message;

@Service
public class UserService {

	JwtUtil jwtUtil = new JwtUtil();
	ModelMapper mappper = new ModelMapper();
	@Autowired
	MailUtility mailUtility;
	@Autowired
	private UserRepository userRepository;

	/**
	 * @purpose : To get list users
	 * @return : List<User>
	 */
	public Response getUsers() {
		List<User> list = new ArrayList<User>();
		userRepository.findAll().forEach(list::add);
		return new Response(Message.STATUS200, Message.ALL_USERS, list);
	}

	/**
	 * @param userDto : store credential data
	 */
	public Response addUser(UserDto userDto) {
		User user = mappper.map(userDto, User.class); // json object convert into user type object
		userRepository.save(user);
		return new Response(Message.STATUS200, Message.USER_ADDED, null);
	}

	/**
	 * @purpose : remove data
	 * @param id :
	 */
	public void removeUser(int id) {
		userRepository.deleteById(id);
	}

	/**
	 * purpose :search user in repository by id
	 * 
	 * @param id : store int type data
	 * 
	 *           }
	 */
	public boolean isVerify(int id) {
		Optional<User> user = userRepository.findById(id);
		System.out.println(user);
		if (user != null) {
			String token = jwtUtil.generateToken(user.get().getEmail());
			String recieveremail = jwtUtil.extractUsername(token);
			String subject = "testing";
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
	public Response loginVerification(LoginDto LoginDto) // dto stands for data transfer object
	{
		User user = userRepository.findByEmail(LoginDto.getEmail());
		if (user != null) {
			if (user.getPassword().equals(user.getPassword())) {
				String token = jwtUtil.generateToken(user.getEmail());
				String subject = "testing";
				String recieveremail = user.getEmail();
				String mailsender = "forgotbridge70@gmail.com";
				String text = "token=" + token;
				System.out.println("mail sending......in....progressssssssssss");
				mailUtility.accountVerification(mailsender, recieveremail, subject, text);

				return new Response(Message.STATUS200, Message.LOGIN_SUCCESS, null);
			}
		}
		return new Response(Message.STATUS200, Message.INVALID, null);
	}

	/**
	 * @purpose : get user
	 * @param token : store string type token data
	 * @return : User
	 */
	public Response getUser(String token) {
		String username = jwtUtil.extractUsername(token);
		User user = userRepository.findByEmail(username);
		List<User> list = new ArrayList<User>();
		list.add(user);
		return new Response(Message.STATUS200, Message.LOGIN_SUCCESS + "token=" + token, list);
	}

	/**
	 * @purpose : To update database
	 * @param token         : store string type data
	 * @param userUpdateDto : store credential data
	 * @return : string
	 */
	public Response update(String token, UserDto userDto) {
		String username = jwtUtil.extractUsername(token);
		User user = userRepository.findByEmail(username);
		if (user != null) {
			user.setName(userDto.getName());
			user.setAddress(userDto.getAddress());
			user.setMobile_no(userDto.getMobile_no());
			userRepository.save(user);
			return new Response(Message.STATUS200, Message.USER_UPDATE, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_FOUND, null);
	}

	/**
	 * @purpose : To change password
	 * @param token             : store string type data
	 * @param forgetPassworddto : store credential data
	 * @return : string
	 */
	public Response resetPassword(String token, ResetPasswordDto forgetPassworddto) {
		String username = jwtUtil.extractUsername(token);
		User user = userRepository.findByEmail(username);

		if (forgetPassworddto.getPassword().equalsIgnoreCase(forgetPassworddto.getConfirmPassword())) {
			user.setPassword(forgetPassworddto.getPassword());
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
//	public String emailVerfiation(int i) {	 
//		                                        //generate token for email verification
//		return jwtUtil.generateToken(username);
//	}

}