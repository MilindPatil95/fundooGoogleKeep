package com.bridgelabz.googlekeep.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelabz.googlekeep.dto.CollaboratorDto;
import com.bridgelabz.googlekeep.dto.DateDto;
import com.bridgelabz.googlekeep.dto.NoteDto;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.service.NoteServiceImp;

@RestController
@RequestMapping("/fundoo/note")
public class NoteController {
	@Autowired
	NoteServiceImp noteService;

	@PostMapping("/create")
	public Response createNote(@Valid @RequestBody NoteDto note, @RequestHeader String token) {
		return noteService.createNote(note, token);
	}

	@PostMapping("/delete")
	public Response deleteNote(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.deleteNote(token, note_id);
	}

	@GetMapping("/get")
	public Response getNotes(@RequestHeader String token) {
		return noteService.getAllNotes(token);
	}

	@PutMapping("/update")
	public Response updateNote(@RequestHeader String token,@Valid @RequestBody NoteDto notedto, @RequestHeader int note_id) {
		 return noteService.update(token, notedto, note_id);
	}

	@PutMapping("/pin")
	@Cacheable(value ="token",key = "#token")
	public Response pinNote(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.pin(token, note_id);
	}

	@PutMapping("/archive")
	public Response archive(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.archive(token, note_id);
	}

	@PutMapping("/getAllArchive")
	public Response getAllArchive(@RequestHeader String token) {
		return noteService.getAllArchive(token);
	}

	@PutMapping("/trash")
	public Response trash(@RequestHeader String token, @RequestHeader int note_id) {
		return noteService.trash(token, note_id);
	}

	@PutMapping("/getAllTrash")
	public Response getAllTrash(@RequestHeader String token) {
		return noteService.getAllTrash(token);
	}


	@PutMapping("/addreminder")
	public Response addReminder(@Valid @RequestBody DateDto datedto,@RequestHeader String token) {
		return noteService.reminder(datedto,token);
	}

	@PutMapping("/editReminder")
	public Response editReminder(@Valid @RequestBody DateDto datedto,@RequestHeader String token) {
		return noteService.editReminder(datedto,token);
	}

	@PutMapping("/deleteReminder")
	public Response deleteReminder(@RequestHeader int note_id,@RequestHeader String token) {
		return noteService.deleteReminder(note_id,token);
	}

	@PutMapping("/collaborator")
	public Response Collaborator(@Valid @RequestBody CollaboratorDto collaboratorDto, @RequestHeader String token) {
		return noteService.collaborator(collaboratorDto, token);
	}

	@GetMapping("/geAllCollaborated")
	public Response getCollaborator(@RequestHeader String token, @RequestHeader int noteid) {
		return noteService.getCollaborated(token,noteid);
	}
	@GetMapping("/sortByName")
	public  Response sortByTitle(@RequestHeader String token)
	{
		 return  noteService.sortByTitle(token);
	}
	@GetMapping("/sortByDscription")
	public  Response sortDscription(@RequestHeader String token)
	{
		 return  noteService.sortByDscription(token);
	}
	

}
