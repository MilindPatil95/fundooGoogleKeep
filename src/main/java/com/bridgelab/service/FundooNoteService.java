package com.bridgelab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelab.dto.NoteDto;
import com.bridgelab.model.Note;
import com.bridgelab.model.User;
import com.bridgelab.repository.FundooNodeRepository;
import com.bridgelab.repository.UserRepository;
import com.bridgelab.response.NoteResponse;
import com.bridgelab.utility.JwtUtil;
import com.bridgelab.utility.Message;

@Service
public class FundooNoteService {
	@Autowired
	FundooNodeRepository fundooNodeRepository;
	@Autowired
	UserRepository userRepository;
	ModelMapper mapper = new ModelMapper();
	JwtUtil jwtUtil = new JwtUtil();

	public NoteResponse createNote(NoteDto notedto, String token) {
		Note note = mapper.map(notedto, Note.class);
		User user = getUser(token);
		if (user != null) {
			note.setUserId(user.getId());
			fundooNodeRepository.save(note);
			return new NoteResponse(Message.STATUS200, Message.ADD_NOTE, null);
		} else
			return new NoteResponse(Message.STATUS401, Message.USER_INVALID, null);
	}

	public NoteResponse deleteNote(String token, int note_id) {
		User user = getUser(token);
		List<Note> list = fundooNodeRepository.findByUserId(user.getId());
		if (list != null) {
			try {
			Note note = (list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList())).get(0);
			if (note != null) {
				if (note.isTrash()) {
					fundooNodeRepository.delete(note);
					return new NoteResponse(Message.STATUS200, Message.DTELETE_NOTE, null);
				}					
			}
			}catch(Exception e)
			{
				return new NoteResponse(Message.STATUS404, Message.NOTE_NOT_PRESENT, null);
			}
		}	return new NoteResponse(Message.STATUS404, Message.NOTES_NOT_PRESENT, null);
		
		
	}

	public NoteResponse geAllNotes(String token) {
		User user = getUser(token);
		if (user != null) {
			List<Note> list = new ArrayList<>();
			fundooNodeRepository.findAll().forEach(list::add);
			list = list.stream().filter(e -> e.isArchive() == false).collect(Collectors.toList());
			return new NoteResponse(Message.STATUS200, Message.NOTES_RETURN, list);
		}
		return new NoteResponse(Message.STATUS401, Message.USER_INVALID, null);
	}

	public NoteResponse update(String token, NoteDto notedto, int note_id) {
		User user = getUser(token);
		if (user != null) {
			List<Note> list = fundooNodeRepository.findByUserId(user.getId());
			if (list != null) {
				Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
				note.setTitle(notedto.getTitle());
				note.setDesctiption(notedto.getDesctiption());
				note.setColor(notedto.getColor());
				fundooNodeRepository.save(note);
				return new NoteResponse(Message.STATUS200, Message.NOTE_UPDATED, null);
			} else
				return new NoteResponse(Message.STATUS403, Message.NOTES_NOT_PRESENT, null);
		} else
			return new NoteResponse(Message.STATUS401, Message.USER_INVALID, null);

	}

	public User getUser(String token) {
		String email = jwtUtil.extractUsername(token);
		return userRepository.findByEmail(email);
	}

	public NoteResponse pin(String token, int note_id) {
		User user = getUser(token);
		if (user != null) {
			List<Note> list = fundooNodeRepository.findByUserId(user.getId());
			if (list != null) {
				try {
					Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
					if (note != null) {
						if (note.isPin()) {
							note.setPin(false);
							fundooNodeRepository.save(note);
							return new NoteResponse(Message.STATUS200, Message.PING, null);
						} else {
							note.setPin(true);
							fundooNodeRepository.save(note);
							return new NoteResponse(Message.STATUS200, Message.UNPING, null);
						}
					}
				} catch (Exception e) {
					return new NoteResponse(Message.STATUS404, Message.NOTE_NOT_PRESENT, null);
				}

			} else
				return new NoteResponse(Message.STATUS404, Message.NOTES_NOT_PRESENT, null);
		}

		return new NoteResponse(Message.STATUS401, Message.USER_INVALID, null);
	}

	public NoteResponse archive(String token, int note_id) {
		User user = getUser(token);
		if (user != null) {
			List<Note> list = fundooNodeRepository.findByUserId(user.getId());
			if (list != null) {
				try {
					Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);

					if (note != null) {
						if (note.isArchive()) {
							note.setArchive(false);
							fundooNodeRepository.save(note);
							return new NoteResponse(Message.STATUS200, Message.UNARCHIVE, null);
						} else {
							note.setArchive(true);
							fundooNodeRepository.save(note);
							return new NoteResponse(Message.STATUS200, Message.ARCHIVE, null);
						}
					}
				} catch (Exception e) {
					return new NoteResponse(Message.STATUS404, Message.NOTE_NOT_PRESENT, null);
				}
			} else
				return new NoteResponse(Message.STATUS404, Message.NOTES_NOT_PRESENT, null);

		}
		return new NoteResponse(Message.STATUS401, Message.USER_INVALID, null);
	}

	public NoteResponse trash(String token, int note_id) {
		User user = getUser(token);
		if (user != null) {
			List<Note> list = fundooNodeRepository.findByUserId(user.getId());
			if (list != null) {
				try {
					Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
					if (note != null) {
						if (note.isTrash()) {
							note.setTrash(false);
							fundooNodeRepository.save(note);
							return new NoteResponse(Message.STATUS200, Message.UNTRASH, null);
						} else {
							note.setTrash(true);
							fundooNodeRepository.save(note);
							return new NoteResponse(Message.STATUS200, Message.TRASH, null);
						}
					}
				} catch (Exception e) {
					return new NoteResponse(Message.STATUS404, Message.NOTE_NOT_PRESENT, null);

				}
			} else
				return new NoteResponse(Message.STATUS404, Message.NOTES_NOT_PRESENT, null);
		}
		return new NoteResponse(Message.STATUS401, Message.USER_INVALID, null);
	}

}
