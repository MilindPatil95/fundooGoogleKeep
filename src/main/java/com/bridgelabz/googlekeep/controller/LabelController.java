package com.bridgelabz.googlekeep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.service.LabelServiceImp;
import com.bridgelabz.googlekeep.service.NoteServiceImp;

@RestController
@RequestMapping("/fundoo/userLabel")
public class LabelController {
	@Autowired
	LabelServiceImp labelService;
	@Autowired
	NoteServiceImp noteService;

	@PostMapping("/createLabel")
	public Response createLabel(@RequestHeader String name, @RequestHeader String token) {
		return labelService.createLabel(name, token);
	}

	@PostMapping("/addLabelToNote")
	public Response addLabel(@RequestHeader int noteid, @RequestHeader int labelid, @RequestHeader String token) {
		return labelService.addLabelToNote(noteid, labelid, token);
	}

	@PostMapping("/deleteLabel")
	public Response deleteLabel(@RequestHeader String token, @RequestHeader int label_id) {
		return labelService.deleteLabel(token, label_id);
	}

	@PostMapping("/getLabels")
	public Response getLabel(@RequestHeader String token) {
		return labelService.getUserLabel(token);
	}

	@PutMapping("/editLabel")
	public Response editLabel(@RequestHeader String token, @RequestHeader int lablelid,
			@RequestHeader String lableName) {
		return labelService.editLabel(token, lablelid, lableName);
	}

	@GetMapping("/getAllNoteLabels")
	public Response getAllNoteLabels(@RequestHeader String token, @RequestHeader int noteid) {
		return noteService.getAllNoteLabels(token, noteid);
	}

}
