package com.bridgelab.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelab.dto.CollaboratorDto;
import com.bridgelab.dto.NoteDto;
import com.bridgelab.model.Collaborator;
import com.bridgelab.model.Note;
import com.bridgelab.model.User;
import com.bridgelab.repository.CollaboratorRepository;
import com.bridgelab.repository.NoteRepository;
import com.bridgelab.repository.UserRepository;
import com.bridgelab.response.LabelResponse;
import com.bridgelab.response.NoteResponse;
import com.bridgelab.utility.JwtUtil;
import com.bridgelab.utility.Message;

@Service
public class NoteService {
	@Autowired
	NoteRepository noteRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CollaboratorRepository collaboratorRepository;
	ModelMapper mapper = new ModelMapper();
	JwtUtil jwtUtil = new JwtUtil();

	public NoteResponse createNote(NoteDto notedto, String token) {
		Note note = mapper.map(notedto, Note.class);
		User user = isUser(token);
		note.setUserId(user.getId());
		noteRepository.save(note);
		return new NoteResponse(Message.STATUS200, Message.ADD_NOTE, null);
	}

	public NoteResponse deleteNote(String token, int note_id) {
		isUser(token);
		Note note = checkNote(note_id);
		if (note.isTrash()) {
			noteRepository.delete(note);
			return new NoteResponse(Message.STATUS200, Message.DTELETE_NOTE, null);
		}
		return new NoteResponse(Message.STATUS404, Message.NOTE_NOT_IN_TRASH, null);

	}

	public NoteResponse geAllNotes(String token) {
		isUser(token);
		List<Note> list = new ArrayList<>();
		noteRepository.findAll().forEach(list::add);
		list = list.stream().filter(e -> e.isArchive() == false).collect(Collectors.toList());
		return new NoteResponse(Message.STATUS200, Message.NOTES_RETURN, list);

	}

	public NoteResponse update(String token, NoteDto notedto, int note_id) {
		List<Note> list = checkNoteByUserId(isUser(token).getId());
		Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
		note.setTitle(notedto.getTitle());
		note.setDesctiption(notedto.getDesctiption());
		note.setColor(notedto.getColor());
		noteRepository.save(note);
		return new NoteResponse(Message.STATUS200, Message.NOTE_UPDATED, null);

	}

	public LabelResponse invalidToken() {
		return new LabelResponse(Message.STATUS403, Message.INVALID_TOKEN, null);
	}

	public NoteResponse pin(String token, int note_id) {
		isUser(token);

		List<Note> list = checkNoteByUserId(isUser(token).getId());
		Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);

		if (note.isPin()) {
			note.setPin(false);
			noteRepository.save(note);
			return new NoteResponse(Message.STATUS200, Message.PING, null);
		} else {
			note.setPin(true);
			noteRepository.save(note);
			return new NoteResponse(Message.STATUS200, Message.UNPING, null);
		}
	}

	public NoteResponse archive(String token, int note_id) {
		isUser(token);

		List<Note> list = checkNoteByUserId(isUser(token).getId());

		Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);

		if (note.isArchive()) {
			note.setArchive(false);
			noteRepository.save(note);
			return new NoteResponse(Message.STATUS200, Message.UNARCHIVE, null);
		} else {
			note.setArchive(true);
			noteRepository.save(note);
			return new NoteResponse(Message.STATUS200, Message.ARCHIVE, null);
		}

	}

	public NoteResponse trash(String token, int note_id) {
		isUser(token);

		List<Note> list = checkNoteByUserId(isUser(token).getId());
		Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
		if (note.isTrash()) {
			note.setTrash(false);
			noteRepository.save(note);
			return new NoteResponse(Message.STATUS200, Message.UNTRASH, null);
		} else {
			note.setTrash(true);
			noteRepository.save(note);
			return new NoteResponse(Message.STATUS200, Message.TRASH, null);
		}

	}

	public NoteResponse reminder(String token, int noteid, int no_of_days) {
		isUser(token);
		List<Note> list = checkNoteByUserId(isUser(token).getId());
		list.stream().filter(e -> e.getNoteId() == noteid).collect(Collectors.toList()).get(0).setReminder(String.valueOf(LocalDateTime.now().plusDays(no_of_days)));
		noteRepository.save(list.get(0));
		return new NoteResponse(Message.STATUS200, Message.REMINDER_SET, null);

	}

	public NoteResponse collaborator(CollaboratorDto collaboratorDto, String token) {
		isUser(token);
		Collaborator collaborator = mapper.map(collaboratorDto, Collaborator.class);
		checkNote(collaborator.getNote_id());
		collaboratorRepository.save(collaborator);
		return new NoteResponse(Message.STATUS200, Message.COLABORATOR_ADDED, null);
	}

	public User isUser(String token) {
		String email = null;
		try {
			email = jwtUtil.extractUsername(token);
		} catch (Exception e) {
			invalidToken();
		}
		return userRepository.findByEmail(email);
	}

	public List<Note> checkNoteByUserId(int userid) {
		List<Note> list = null;
		try {
			list = noteRepository.findByUserId(userid);

		} catch (Exception e) {
			emptyListResponse();
		}
		return list;

	}

	public Note checkNote(int note_id) {
		Note note = null;
		try {
			Optional<Note> optionalnote = noteRepository.findById(note_id);
			note = optionalnote.get();

		} catch (Exception e) {
			invalidNoteResponse();
		}
		return note;

	}

	private NoteResponse invalidNoteResponse() {
		return new NoteResponse(Message.STATUS404, Message.NOTE_NOT_PRESENT, null);
	}

	private NoteResponse emptyListResponse() {

		return new NoteResponse(Message.STATUS404, Message.NOTES_NOT_PRESENT, null);
	}

}
