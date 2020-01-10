package com.bridgelab.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelab.dto.NoteDto;
import com.bridgelab.response.NoteResponse;
import com.bridgelab.service.FundooNoteService;
@RestController
@RequestMapping("/fundoo/note")
public class FundooNoteController 
{     @Autowired
	  FundooNoteService fundooNoteService;
	   @PostMapping("/create")
	   public NoteResponse createNote(@RequestBody NoteDto note,@RequestHeader String token)
	   {
		    return fundooNoteService.createNote(note,token);
	   }  
	   @PostMapping("/delete")
	   public NoteResponse deleteNote(@RequestHeader String token,@RequestHeader int note_id)
	   {
		  return fundooNoteService.deleteNote(token,note_id);
	   }
	   @GetMapping("/get")
	   public NoteResponse getNotes(@RequestHeader String token)
	   {
		  return  fundooNoteService.geAllNotes(token);	   
	   }
	   @PutMapping("/update")
	   public void updateNote(@RequestHeader String token,@RequestBody NoteDto notedto,@RequestHeader int note_id)
	   {
		   fundooNoteService.update(token,notedto,note_id);
	   }
	   @PutMapping("/pin")
	   public NoteResponse pinNote(@RequestHeader String token,@RequestHeader int note_id)
	   {
             return fundooNoteService.pin(token,note_id);    		   
	   }
	   @PutMapping("/archive")
	   public NoteResponse archive(@RequestHeader String token,@RequestHeader int note_id)
	   {
		 return  fundooNoteService.archive(token,note_id);
	   }
	   	   
	   @PutMapping("/trash")
	   public NoteResponse trash(@RequestHeader String token,@RequestHeader int note_id)
	   {
		   return fundooNoteService.trash(token,note_id);
	   }
	   
}
