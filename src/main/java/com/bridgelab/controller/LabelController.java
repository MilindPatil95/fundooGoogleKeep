package com.bridgelab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelab.response.LabelResponse;
import com.bridgelab.service.LabelService;

@RestController
@RequestMapping("/fundoo/userLabel")
public class LabelController {
	@Autowired
	LabelService labelService;

	@PostMapping("/createLabel")
	public LabelResponse createLabel(@RequestHeader String name, @RequestHeader String token) {
		return labelService.createLabel(name, token);
	}

	@PostMapping("/addLabel")
	public LabelResponse addLabel(@RequestHeader int userid, @RequestHeader String token) {
		return labelService.addLabel(userid, token);
	}

	@PostMapping("/deleteLabel")
	public LabelResponse deleteLabel(@RequestHeader String token, @RequestHeader int label_id) {
		return labelService.deleteLabel(token, label_id);
	}

	@PostMapping("/geLabels")
	public LabelResponse getLabel(@RequestHeader String token) {
		return labelService.getUserLabel(token);
	}
	@PutMapping("/editLabel")
	public LabelResponse editLabel(@RequestHeader String token,@RequestHeader int lablelid,@RequestHeader String lableName)
	{
		return labelService.editLabel(token, lablelid, lableName);
	}
	
}
