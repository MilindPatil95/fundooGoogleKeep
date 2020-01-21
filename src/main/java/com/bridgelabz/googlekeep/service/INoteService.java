package com.bridgelabz.googlekeep.service;
import com.bridgelabz.googlekeep.dto.CollaboratorDto;
import com.bridgelabz.googlekeep.dto.DateDto;
import com.bridgelabz.googlekeep.dto.NoteDto;
import com.bridgelabz.googlekeep.response.Response;

public interface INoteService {
	public Response createNote(NoteDto notedto, String token);
	public Response deleteNote(String token, int note_id);
	public Response getAllNotes(String token);
	public Response update(String token, NoteDto notedto, int note_id);
	public Response pin(String token, int note_id);
	public Response archive(String token, int note_id);
	public Response getAllArchive(String token);
	public Response getAllTrash(String token);
	public Response trash(String token, int note_id);
	public Response reminder(DateDto datedto,String token);
	public Response editReminder(DateDto datedto,String token);
	public Response deleteReminder(int noteid, String token);
	public Response collaborator(CollaboratorDto collaboratorDto, String token);
	public Response getCollaborated(String token,int noteid);
	public Response getAllNoteLabels(String token, int noteid);


}
