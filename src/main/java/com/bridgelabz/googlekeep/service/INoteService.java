package com.bridgelabz.googlekeep.service;


import com.bridgelabz.googlekeep.dto.CollaboratorDto;
import com.bridgelabz.googlekeep.dto.NoteDto;
import com.bridgelabz.googlekeep.response.Response;

public interface INoteService {
	public Response createNote(NoteDto notedto, String token);
	public Response deleteNote(String token, int note_id);
	public Response geAllNotes(String token);
	public Response update(String token, NoteDto notedto, int note_id);
	public Response pin(String token, int note_id);
	public Response archive(String token, int note_id);
	public Response geAllArchive(String token);
	public Response geAllTrash(String token);
	public Response trash(String token, int note_id);
	public Response reminder(String token, int noteid, String reminderdate);
	public Response editReminder(String token, int noteid, String reminderdate);
	public Response deleteReminder(String token, int note_id, String reminderdate);
	public Response collaborator(CollaboratorDto collaboratorDto, String token);
	public Response getCollaborated(String token,int noteid);
	public Response getAllNoteLabels(String token, int noteid);


}
