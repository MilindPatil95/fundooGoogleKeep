package com.bridgelabz.googlekeep.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.googlekeep.CustomException.CustomException;
import com.bridgelabz.googlekeep.model.NoteLabel;
import com.bridgelabz.googlekeep.model.User;
import com.bridgelabz.googlekeep.model.UserLabel;
import com.bridgelabz.googlekeep.repository.LabelRepository;
import com.bridgelabz.googlekeep.repository.NoteLabelRepository;
import com.bridgelabz.googlekeep.repository.RedisRepositoryImpl;
import com.bridgelabz.googlekeep.repository.UserRepository;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.utility.JwtUtil;
import com.bridgelabz.googlekeep.utility.Message;

@Service
public class LabelServiceImp implements ILabelService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserServiceImp userService;
	@Autowired
	NoteServiceImp noteService;
	@Autowired
	LabelRepository labelRepository;
	@Autowired
	NoteLabelRepository noteLabelRepository;
	@Autowired
	RedisRepositoryImpl redisRepositoryImpl;

	ModelMapper mapper = new ModelMapper();
	JwtUtil jwtUtil = new JwtUtil();
	static int flag = 0;

	/**
	 * @purpose : To add Label to note
	 * @param :token
	 * @param :note_id
	 * @param :label   id
	 * @return :Respose type
	 */
	public Response addLabelToNote(int noteid, int labelid, String token) {
		if (redisRepositoryImpl.findUser(token) != null) { // Check login user login or not
			userService.isUser(token);
			noteService.checkNoteByNoteId(noteid);
			Optional<UserLabel> label = labelRepository.findById(labelid);

			jwtUtil.checkLabel(label.get());
			NoteLabel notelabel = new NoteLabel();
			notelabel.setNoteid(noteid);
			notelabel.setLabelid(labelid);
			noteLabelRepository.save(notelabel);
			return new Response(Message.STATUS200, Message.LABEL_ADDED, notelabel);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose :To create Label
	 * @param :token
	 * @param :name  of label
	 * @param :      title of label
	 * @return :Respose type
	 */
	public Response createLabel(String name, String token) {
		if (redisRepositoryImpl.findUser(token) != null) { // Check login user login or not
			
		User user = userService.isUser(token);
		UserLabel label = new UserLabel();
		label.setLabelname(name);
		label.setUserId(user.getId());
		labelRepository.save(label);
		return new Response(Message.STATUS200, Message.LABEL_ADDED, label);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose :To delete Label
	 * @param :token
	 * @param :id
	 * @return :Respose type
	 */
	public Response deleteLabel(String token, int id) {
		if (redisRepositoryImpl.findUser(token) != null) { // Check login user login or not
			
		User user = userService.isUser(token);
		Optional<UserLabel> label = labelRepository.findById(id);
		jwtUtil.checkLabel(label.get());
		List<UserLabel> list = labelRepository.findAllByUserId(user.getId());
		if (list != null) {
			UserLabel label1 = list.stream().filter(e -> e.getLabel_id() == id).collect(Collectors.toList()).get(0);
			labelRepository.deleteById(label1.getLabel_id());
			return new Response(Message.STATUS200, Message.LABEL_DELETED, null);
		} else {
			return new Response(Message.STATUS403, Message.LABEL_NOT_FOUND, null);
		}
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);


	}

	/**
	 * @purpose :To edit Label
	 * @param :token
	 * @param :id
	 * @return :Respose type
	 */
	public Response editLabel(String token, int id, String name) {
		if (redisRepositoryImpl.findUser(token) != null) { // Check login user login or not
			User user = userService.isUser(token);
		Optional<UserLabel> labellist = labelRepository.findById(id);
		jwtUtil.checkLabel(labellist.get());
		List<UserLabel> list = labelRepository.findAllByUserId(user.getId());
		if (list != null) {
			List<Object> editlabellist = new ArrayList<>();
			UserLabel label = list.stream().filter(e -> e.getLabel_id() == id).collect(Collectors.toList()).get(0);
			label.setLabelname(name);
			labelRepository.save(label);
			editlabellist.add(label);
			return new Response(Message.STATUS200, Message.LABEL_EDIT, editlabellist);
		} else {
			return new Response(Message.STATUS403, Message.LABEL_NOT_FOUND, null);
		}
	}
	return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * SS
	 * 
	 * @purpose :To get all Labels of user
	 * @param :token
	 * @return :Respose type
	 */
	public Response getUserLabel(String token) {
		if (redisRepositoryImpl.findUser(token) != null) { // Check login user login or not
			User user = userService.isUser(token);
		if (user != null) {
			List<UserLabel> labellist = labelRepository.findAllByUserId(user.getId());
			return new Response(Message.STATUS200, Message.ALL_LABLE, labellist);
		}
		return new Response(Message.STATUS404, Message.USER_NOT_FOUND, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}
	/**
	 * @purpose :To check token is valid or not
	 * @param : -
	 * @return :Respose type
	 */

}
