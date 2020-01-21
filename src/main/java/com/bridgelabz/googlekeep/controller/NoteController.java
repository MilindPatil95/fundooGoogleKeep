package com.bridgelabz.googlekeep.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.googlekeep.dto.CollaboratorDto;
import com.bridgelabz.googlekeep.dto.NoteDto;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.service.NoteServiceImp;

@RestController
@RequestMapping("/fundoo/note")
public class NoteController {
	@Autowired
	NoteServiceImp noteService;

	@PostMapping("/create")
	public Response createNote(@RequestBody NoteDto note, @RequestHeader String token) {
		return noteService.createNote(note, token);
	}

	@PostMapping("/delete")
	public Response deleteNote(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.deleteNote(token, note_id);
	}

	@GetMapping("/get")
	public Response getNotes(@RequestHeader String token) {
		return noteService.geAllNotes(token);
	}

	@PutMapping("/update")
	public void updateNote(@RequestHeader String token, @RequestBody NoteDto notedto, @RequestHeader int note_id) {
		noteService.update(token, notedto, note_id);
	}

	@PutMapping("/pin")
	public Response pinNote(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.pin(token, note_id);
	}

	@PutMapping("/archive")
	public Response archive(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.archive(token, note_id);
	}

	@PutMapping("/getAllArchive")
	public Response geAllArchive(@RequestHeader String token) {
		return noteService.geAllArchive(token);
	}

	@PutMapping("/trash")
	public Response trash(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.trash(token, note_id);
	}

	@PutMapping("/getAllTrash")
	public Response geAllTrash(@RequestHeader String token) {
		return noteService.geAllTrash(token);
	}

	@GetMapping("/reminder")
	public Response addReminder(@RequestHeader String token, @RequestHeader int note_id,@RequestHeader String reminderdate) {
		return noteService.reminder(token, note_id, reminderdate);
	}

	@PutMapping("/editReminder")
	public Response editReminder(@RequestHeader String token, @RequestHeader int note_id,@RequestHeader String reminderdate) {
		return noteService.editReminder(token, note_id,reminderdate);
	}

	@PutMapping("/deleteReminder")
	public Response deleteReminder(@RequestHeader String token, @RequestHeader int note_id,@RequestHeader String reminderdate) {
		return noteService.deleteReminder(token, note_id ,reminderdate);
	}

	@PutMapping("/collaborator")
	public Response Collaborator(@RequestBody CollaboratorDto collaboratorDto, @RequestHeader String token) {
		return noteService.collaborator(collaboratorDto, token);
	}

	@GetMapping("/geAllCollaborated")
	public Response getCollaborator(@RequestHeader String token, @RequestHeader int noteid) {
		return noteService.getCollaborated(token,noteid);
	}

}
