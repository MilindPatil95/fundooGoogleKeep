package com.bridgelab.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bridgelab.model.User;
import com.bridgelab.model.UserLabel;
import com.bridgelab.repository.LabelRepository;
import com.bridgelab.repository.UserRepository;
import com.bridgelab.response.LabelResponse;
import com.bridgelab.utility.JwtUtil;
import com.bridgelab.utility.Message;

@Service
public class LabelService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	LabelRepository labelRepository;
	ModelMapper mapper = new ModelMapper();
	JwtUtil jwtUtil = new JwtUtil();
	static int flag = 0;

	public LabelResponse addLabel(int userid, String token) {

		return null;
	}

	public LabelResponse createLabel(String name, String token) {

		List<UserLabel> list = new ArrayList<>();
		UserLabel label = new UserLabel();
		User user = getUser(token);
		if (user != null) {
			label.setLabel(name);
			label.setUserId(user.getId());
			labelRepository.save(label);
			list.add(label);
			return new LabelResponse(Message.STATUS200, Message.LABEL_ADDED, list);
		} else
			return new LabelResponse(Message.STATUS404, Message.LABEL_NOT_ADDED, null);
	}

	public User getUser(String token) {
		String email = null;
		try {
			email = jwtUtil.extractUsername(token);
		} catch (Exception e) {
			invalidToken();
		}
		return userRepository.findByEmail(email);
	}

	public LabelResponse deleteLabel(String token, int id) {
		User user = getUser(token);

		if (user != null) {
			List<UserLabel> list = labelRepository.findAllByUserId(user.getId());
			if (list != null) {
				try {
					UserLabel label1 = list.stream().filter(e -> e.getLabel_id() == id).collect(Collectors.toList())
							.get(0);
					labelRepository.deleteById(label1.getLabel_id());
					return new LabelResponse(Message.STATUS200, Message.LABEL_DELETED, null);
				} catch (Exception e) {
					return new LabelResponse(Message.STATUS403, Message.INVALID_ID, null);
				}
			} else
				return new LabelResponse(Message.STATUS404, Message.LABEL_NOT_FOUND, null);
		}
		return new LabelResponse(Message.STATUS403, Message.INVALID_TOKEN, null);
	}

	public LabelResponse editLabel(String token, int id, String name) {
		User user = getUser(token);
		if (user != null) {
			List<UserLabel> list = labelRepository.findAllByUserId(user.getId());
			if (list != null) {
				try {
					List<UserLabel> editlabellist = new ArrayList<>();
					UserLabel label = list.stream().filter(e -> e.getLabel_id() == id).collect(Collectors.toList())
							.get(0);
					label.setLabel(name);
					labelRepository.save(label);
					editlabellist.add(label);
					return new LabelResponse(Message.STATUS200, Message.LABEL_DELETED, editlabellist);
				} catch (Exception e) {
					return new LabelResponse(Message.STATUS403, Message.INVALID_ID, null);
				}
			} else
				return new LabelResponse(Message.STATUS404, Message.LABEL_NOT_FOUND, null);
		}
		return new LabelResponse(Message.STATUS404, Message.USER_NOT_FOUND, null);
	}

	public LabelResponse getUserLabel(String token) {
		User user = getUser(token);
		if (user != null) {

			List<UserLabel> labellist = labelRepository.findAllByUserId(getUser(token).getId());
			return new LabelResponse(Message.STATUS200, Message.ALL_LABLE, labellist);
		}
		return new LabelResponse(Message.STATUS404, Message.USER_NOT_FOUND, null);
	}

	public LabelResponse invalidToken() {
		return new LabelResponse(Message.STATUS403, Message.INVALID_TOKEN, null);
	}

	
}
