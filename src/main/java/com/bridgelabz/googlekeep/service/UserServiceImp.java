package com.bridgelabz.googlekeep.service;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.bridgelabz.googlekeep.CustomException.CustomException;
import com.bridgelabz.googlekeep.dto.LoginDto;
import com.bridgelabz.googlekeep.dto.ResetPasswordDto;
import com.bridgelabz.googlekeep.dto.UserDto;
import com.bridgelabz.googlekeep.model.DataContainer;
import com.bridgelabz.googlekeep.model.RedisModel;
import com.bridgelabz.googlekeep.model.User;
import com.bridgelabz.googlekeep.rabbitmqservice.RabbitMQSender;
import com.bridgelabz.googlekeep.repository.RedisRepositoryImpl;
import com.bridgelabz.googlekeep.repository.UserRepository;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.utility.JwtUtil;
import com.bridgelabz.googlekeep.utility.MailUtility;
import com.bridgelabz.googlekeep.utility.Message;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

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
	@Autowired
	RedisRepositoryImpl redisRepository;
	@Autowired
	RabbitMQSender rabbitMQSender;
	RedisModel redisModel = new RedisModel();

	/**
	 * @purpose : To get list users
	 * @return : List<User>
	 */
	@Override
	public Response getUsers() {
		List<Object> list = new ArrayList<Object>();
		userRepository.findAll().forEach(list::add); // make list of users
		return new Response(Message.STATUS200, Message.ALL_USERS, list);
	}

	/**
	 * @purpose : To add user
	 * @param userDto : store credential data
	 * @retrun : Response type
	 */
	@Override
	public Response addUser(UserDto userDto) {
		User user = mappper.map(userDto, User.class);
		user.setPassword(passwordEncoder.encode(userDto.getPassword())); // encode user password and set in user type
		System.out.println("encoded");
		User checkuser = userRepository.findByEmail(userDto.getEmail());
		if (checkuser == null) {	
			userRepository.save(user);
			isVerify(user.getEmail());
			return new Response(Message.STATUS200, Message.USER_ADDED, null);
		}
		return new Response(Message.STATUS200, Message.USER_EXIST, null);
	}

//	/**
//	 * @purpose : To remove details
//	 * @param id : int type
//	 * @return :Response type
//	 */
//	@Override
//	public Response removeUser(String token, int id) {
//		if (redisRepository.findUser(token) != null) {
//			isUser(token);
//	   
//			User user = userRepository.findById(id) // use of orElseThrow
//					.orElseThrow(() -> new CustomException.UserNotExistException("user Not Exist"));
//			if (jwtUtil.checkUserById(user)) {
//				userRepository.deleteById(id);
//				return new Response(Message.STATUS200, Message.USER_REMOVE, null);
//			} else
//				return new Response(Message.STATUS200, Message.USER_NOT_FOUND, null);
//		} else
//			return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);
//	}

	
	/**
	 * purpose :search user in repository by id
	 * 
	 * @param id : store int type data
	 */
	@Override
	public boolean isVerify(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			String token = jwtUtil.generateToken(email);
//			String recieveremail = jwtUtil.extractUsername(token);
//			String subject = "Verification token";
//			String mailsender = "forgotbridge70@gmail.com";
//			String text = "token=" + token;
//			System.out.println("mail sending......in....progressssssssssss");
//			mailUtility.accountVerification(mailsender, recieveremail, subject, text); // send mail for verification
			DataContainer dataobj=new DataContainer();
			dataobj.setEmailId(jwtUtil.extractUsername(token));
			dataobj.setMassage("token="+token);
			rabbitMQSender.send(dataobj);			
			return true;
		} else
			return false;
	}
	
	/**
	 * @purpose : login verification
	 * @param logindto : store credential data
	 * @return : Response type
	 */
	@Override
	public Response login(LoginDto LoginDto) // dto stands for data transfer object
	{
		User user = userRepository.findByEmail(LoginDto.getEmail());
		if (user.isVerified()==true) {
			if (passwordEncoder.matches(LoginDto.getPassword(), user.getPassword())) { // matchig repository password
																						// and user input dto password
				String token = jwtUtil.generateToken(user.getEmail());
				redisModel.setToken(token); // add token and email id in redisModel
				redisModel.setLastLogin(LocalDate.now().toString()); // add date
				redisModel.setEmail(passwordEncoder.encode(user.getEmail()));
				redisRepository.add(redisModel);
																			
				return new Response(Message.STATUS200, Message.LOGIN_SUCCESS, token);
			} else
				return new Response(Message.STATUS200, Message.INVALID_PASSWORD, null);
		}
		return new Response(Message.STATUS200, Message.INVALID, null);
	}
  
	/**
	 * @purpose : To update database
	 * @param token         : store string type data
	 * @param userUpdateDto : store credential data
	 * @return : Response type
	 */
	@Override
	public Response update(UserDto userDto) {
			User user = userRepository.findByEmail(userDto.getEmail());
			if (user.isVerified()) {
				if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) { // check user password is
																							// valid or not
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
	 * @return : Response type
	 */
	public Response resetPassword(int  userid, ResetPasswordDto forgetPassworddto) {
	    User user =userRepository.findById(userid).orElseThrow(()->new CustomException.UserNotExistException("user not exist"));	
			if (user != null) {

	     if (forgetPassworddto.getPassword().equals(forgetPassworddto.getConfirmPassword())) {   // check user
			user.setPassword(passwordEncoder.encode(forgetPassworddto.getPassword()));
			userRepository.save(user);
			return new Response(Message.STATUS200, Message.PASSWORD_UDATED, null);
		}
		return new Response(Message.STATUS200, Message.INVALID, null);
			}
			return new Response(Message.STATUS200, Message.USER_NOT_FOUND, null);
		
	}


//	@Override
//	public Response resetPassword(String token, ResetPasswordDto forgetPassworddto) {
//
//		if (redisRepository.findUser(token) != null) {
//			User user = isUser(token);
//			if (forgetPassworddto.getPassword().equals(forgetPassworddto.getConfirmPassword())) {   // check user
//				user.setPassword(passwordEncoder.encode(forgetPassworddto.getPassword()));
//				userRepository.save(user);
//				return new Response(Message.STATUS200, Message.PASSWORD_UDATED, null);
//			}
//			return new Response(Message.STATUS200, Message.INVALID, null);
//		} else
//			return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);
//
//	}

	/**
	 * @purpose : to check user
	 * @param token : string type
	 * @return : Response type
	 */
	@Override
	public User isUser(String token) {
		String email = jwtUtil.validateToken(token); // check tocken valid or not
		return userRepository.findByEmail(email);
	}

	/**
	 * @purpose : get user details by token
	 * @param token : String type
	 * @return : Response type
	 */
	@Override
	public Response getUser(int id) {
		User user=userRepository.findById(id).orElseThrow(()->new CustomException.UserNotExistException("invalid userid"));
	     if(user!=null) { 	
		List<Object> list = new ArrayList<Object>();
			list.add(user);
			return new Response(Message.STATUS200, Message.USER_DATA, list);
		} else
			return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To verify email id for reset password
	 * @param email : String type
	 * @return : Response type
	 */
	@Override
	public Response forgetPasssword(String email) {
		boolean status = isVerify(email); // verify email id
		if (status) {
			return new Response(Message.STATUS200, Message.TOKEN_GENRATED, null);
		}
		return new Response(Message.STATUS200, Message.INVALID_EMAILID, null);
	}

	/**
	 * @purpose : To upload file on Cloudinary and save Cloudinary path on database
	 * @param file  : get MultipartFile
	 * @param token : string type
	 * @return : Response type
	 */
	public Response saveProfile(MultipartFile file, String token) {
			User user = isUser(token);
			File fileobj = new File(file.getOriginalFilename()); // get file name from file object(Multipart file)
			Map<String, String> map = new HashMap<>();
			map.put("cloud_name", "del7lj7f0"); // create map objec and set cloud name,api key, api secret key
			map.put("api_key", "398587234215416");
			map.put("api_secret", "107j26oL6ggAo4_mxjWyUCJOGzI");
			Cloudinary cloudinary = new Cloudinary(map); // set map object in cloudinary constructor
			Map<?, ?> uploadedResult = null;
			try {
				uploadedResult = cloudinary.uploader().upload(fileobj, ObjectUtils.emptyMap());
			} catch (Exception e) {
				throw new CustomException.ProfileNotSave("profile not uploaded");
			}
			user.setProfilepath(uploadedResult.get("secure_url").toString()); // get secure url from uploaded result and
																				// set
																				// into user object
			userRepository.save(user);

			return new Response(Message.STATUS200, Message.PROFILE_SAVE_SUCCESSFULLY, null);
		
	}

	public Response logOut(String token) {
		RedisModel redisModel = redisRepository.findUser(token);
		if (redisModel != null) {
			redisRepository.delete(token);
			return new Response(Message.STATUS200, Message.USER_LOGOUT, null);
		}
		return new Response(Message.STATUS200, Message.USER_ALREADY_LOGOUT, null);
	}

	public Response isverifiedToken(String token) {
            User user=  isUser(token);
            user.setVerified(true);
            userRepository.save(user);
            return new Response(Message.STATUS200, Message.TOKEN_VERIFIED_SUCCESSFULL,null);
	}

}