package com.bridgelab.controller;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelab.dto.CollaboratorDto;
import com.bridgelab.dto.NoteDto;
import com.bridgelab.response.NoteResponse;
import com.bridgelab.service.NoteService;
@RestController
@RequestMapping("/fundoo/note")
public class NoteController 
{     @Autowired
	   NoteService noteService;
	   @PostMapping("/create")
	   public NoteResponse createNote(@RequestBody NoteDto note,@RequestHeader String token)
	   {
		    return noteService.createNote(note,token);
	   }  
	   @PostMapping("/delete")
	   public NoteResponse deleteNote(@RequestHeader String token,@RequestHeader int note_id)
	   {
		  return noteService.deleteNote(token,note_id);
	   }
	   @GetMapping("/get")
	   public NoteResponse getNotes(@RequestHeader String token)
	   {
		  return  noteService.geAllNotes(token);	   
	   }
	   @PutMapping("/update")
	   public void updateNote(@RequestHeader String token,@RequestBody NoteDto notedto,@RequestHeader int note_id)
	   {
		   noteService.update(token,notedto,note_id);
	   }
	   @PutMapping("/pin")
	   public NoteResponse pinNote(@RequestHeader String token,@RequestHeader int note_id)
	   {
             return noteService.pin(token,note_id);    		   
	   }
	   @PutMapping("/archive")
	   public NoteResponse archive(@RequestHeader String token,@RequestHeader int note_id)
	   {
		 return  noteService.archive(token,note_id);
	   }
	   	   
	   @PutMapping("/trash")
	   public NoteResponse trash(@RequestHeader String token,@RequestHeader int note_id)
	   {
		   return noteService.trash(token,note_id);
	   }
       @GetMapping("/reminder")	   
	   public NoteResponse reminder(@RequestHeader String token, @RequestHeader int note_id ,@RequestHeader int no_of_days)
		{
		    return noteService.reminder(token,note_id,no_of_days);
		}
       @PutMapping("/collaborator")
       public void Collaborator(@RequestBody CollaboratorDto collaboratorDto,@RequestHeader String token)
       {
    	   noteService.collaborator(collaboratorDto,token);
       }
	   
}
