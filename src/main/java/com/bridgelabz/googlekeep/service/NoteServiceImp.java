package com.bridgelabz.googlekeep.service;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.googlekeep.CustomException.CustomException;
import com.bridgelabz.googlekeep.dto.CollaboratorDto;
import com.bridgelabz.googlekeep.dto.NoteDto;
import com.bridgelabz.googlekeep.model.Collaborated;
import com.bridgelabz.googlekeep.model.Collaborator;
import com.bridgelabz.googlekeep.model.Note;
import com.bridgelabz.googlekeep.model.NoteLabel;
import com.bridgelabz.googlekeep.model.User;
import com.bridgelabz.googlekeep.model.UserLabel;
import com.bridgelabz.googlekeep.repository.CollaboratorRepository;
import com.bridgelabz.googlekeep.repository.LabelRepository;
import com.bridgelabz.googlekeep.repository.NoteLabelRepository;
import com.bridgelabz.googlekeep.repository.NoteRepository;
import com.bridgelabz.googlekeep.repository.UserRepository;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.utility.JwtUtil;
import com.bridgelabz.googlekeep.utility.Message;

@Service
public class NoteServiceImp implements INoteService{
	@Autowired
	NoteRepository noteRepository;
	@Autowired
	UserServiceImp userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CollaboratorRepository collaboratorRepository;
	@Autowired
	NoteLabelRepository noteLabelRepository;
	@Autowired
	LabelRepository userlabelRepository;
	ModelMapper mapper = new ModelMapper();
	JwtUtil jwtUtil = new JwtUtil();

	/**
	 * @purpose: To create note
	 * @param :DTO   - notedto store cedential data
	 * @param :token (String type)
	 * @return :Response type
	 */
	@Override
	public Response createNote(NoteDto notedto, String token) {
		Note note = mapper.map(notedto, Note.class);
		List<Note> list = new ArrayList<Note>();
		User user = userService.isUser(token);
		note.setUserId(user.getId());
		noteRepository.save(note);
		list.add(note);
		return new Response(Message.STATUS200, Message.ADD_NOTE, list);
	}

	/**
	 * @purpose :To delete note
	 * @param :token
	 * @param :note_id (int type)
	 * @return :Respose type
	 */
	@Override
	public Response deleteNote(String token, int note_id) {
		userService.isUser(token);
		Note note = checkNote(note_id);
		if (note.isTrash()) {
			noteRepository.delete(note);
			return new Response(Message.STATUS200, Message.DTELETE_NOTE, null);
		}
		return new Response(Message.STATUS404, Message.NOTE_NOT_IN_TRASH, null);

	}

	/**
	 * @purpose :To get all notes
	 * @param :token
	 * @param :note_id (int type)
	 * @param :DTO     - notedto store cedential data
	 * @return :Respose type
	 */
	@Override
	public Response geAllNotes(String token) {
		User user = userService.isUser(token);
		List<Note> list = new ArrayList<Note>();     
		List<Object> obj=new ArrayList<Object>();
		list=noteRepository.findByUserId(user.getId());
	    for(Note note:list)
	    {	if(note.isArchive()==false)
	       { if(note.isTrash()==false)
	         {
	           obj.add(note);
	          obj.add(getAllNoteLabels(token, note.getNoteId()).getObj());
	          List<Collaborator> collboratorlist= collaboratorRepository.findAllByNoteid(note.getNoteId());
	          obj.add(collboratorlist);
	         }
	       }
	     }                
		return new Response(Message.STATUS200, Message.NOTES_RETURN, obj);

	}

	/**
	 * @purpose :To update note
	 * @param :token
	 * @param :note_id (int type)
	 * @param :DTO     - notedto store cedential data
	 * @return :Respose type
	 */
	@Override
	public Response update(String token, NoteDto notedto, int note_id) {
		Note  note=checkNote(note_id);
		List<Note> list = checkNoteByUserId(userService.isUser(token).getId());	
		System.out.println(list);
		System.out.println(note);
		note.setTitle(notedto.getTitle());
		
		note.setDesctiption(notedto.getDesctiption());
		note.setColor(notedto.getColor());
		noteRepository.save(note);
		return new Response(Message.STATUS200, Message.NOTE_UPDATED, null);

	}

	/**
	 * @purpose :pin and unpin to note
	 * @param 	:token
	 * @param 	:note_id (int type)
	 * @return :Respose type
	 */@Override
	public Response pin(String token, int note_id) {
		List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
		checkNote(note_id);
		List<Collaborator> collaboratorlist = collaboratorRepository.findAllByNoteid(note_id);
		if (collaboratorlist.isEmpty() == false) {
			System.out.println("collaborator" + collaboratorlist);
			System.out.println("inside collaboratorlist");
			for (Collaborator collaborator : collaboratorlist) {
				if (collaborator.isPin()) {
					collaborator.setPin(false);
					collaboratorRepository.save(collaborator);
					return new Response(Message.STATUS200, Message.UNPING, null);
				} else {
					collaborator.setPin(true);
					collaboratorRepository.save(collaborator);
					return new Response(Message.STATUS200, Message.PING, null);
				}
			}
		} else {
			Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);

			if (note.isPin()) {
				note.setPin(false);
				noteRepository.save(note);
				return new Response(Message.STATUS200, Message.UNPING, null);
			} else {
				note.setPin(true);
				noteRepository.save(note);
				return new Response(Message.STATUS200, Message.PING, null);
			}
		}
		return null;

	}

	/**
	 * @purpose :archive and archive to note
	 * @param :token
	 * @param :note_id
	 * @return :Respose type
	 */
	 @Override
	public Response archive(String token, int note_id) {

		List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
		checkNote(note_id);
		List<Collaborator> collaboratorlist = collaboratorRepository.findAllByNoteid(note_id);
		if (collaboratorlist.isEmpty() == false) {
			System.out.println("collaborator" + collaboratorlist);
			System.out.println("inside collaboratorlist");
			for (Collaborator collaborator : collaboratorlist) {
				if (collaborator.isArchive()) {
					collaborator.setArchive(false);
					collaboratorRepository.save(collaborator);
					return new Response(Message.STATUS200, Message.UNARCHIVE, null);
				} else {
					collaborator.setArchive(true);
					collaboratorRepository.save(collaborator);
					return new Response(Message.STATUS200, Message.ARCHIVE, null);
				}
			}
		} else {
			Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);

			if (note.isArchive()) {
				note.setArchive(false);
				noteRepository.save(note);
				return new Response(Message.STATUS200, Message.UNARCHIVE, null);
			} else {
				note.setArchive(true);
				noteRepository.save(note);
				return new Response(Message.STATUS200, Message.ARCHIVE, null);
			}
		}
		return null;

	}

	/**
	 * @purpose : To get all archive notes
	 * @param :token
	 * @return :Respose type
	 */
	 @Override
	public Response geAllArchive(String token) {
		List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
		List<Note> list1 = list.stream().filter(e -> e.isArchive() == true).collect(Collectors.toList());
		return new Response(Message.STATUS200, Message.ALL_ARCHIVE, list1);
	}

	/**
	 * @purpose :	archive and archive to note
	 * @param 	:	token
	 * @param 	:	note_id
	 * @return 	:	Respose type
	 */
	 @Override
	public Response geAllTrash(String token) {
		List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
		List<Note> list1 = list.stream().filter(e -> e.isTrash() == true).collect(Collectors.toList());
		return new Response(Message.STATUS200, Message.ALL_TRASH, list1);
	}

	/**
	 * @purpose : 	To trash perticular note
	 * @param 	:	token
	 * @param 	:	note_id
	 * @return 	:	Respose type
	 */
	 @Override
	public Response trash(String token, int note_id) {
		 checkNote(note_id);
		List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
		Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
		if (note.isTrash()) {
			note.setTrash(false);
			noteRepository.save(note);
			return new Response(Message.STATUS200, Message.UNTRASH, null);
		} else {
			note.setTrash(true);
			noteRepository.save(note);
			return new Response(Message.STATUS200, Message.TRASH, null);
		}

	}

	/**
	 * @purpose :   To set Reminder
	 * @param   :	token
	 * @param   :   note_id 
	 * @return  :   Respose type
	 */
	 @Override
	public Response reminder(String token, int noteid, String reminderdate) {

		List<Object> datelist = getDates(reminderdate);
		checkNote(noteid);
		Date systemdate = (Date) datelist.get(0);
		Date userdate = (Date) datelist.get(1);
		if (userdate.after(systemdate)) {
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
			Note note = list.stream().filter(e -> e.getNoteId() == noteid).collect(Collectors.toList()).get(0);
			note.setReminder(systemdate.toString());
			noteRepository.save(list.get(0));
			return new Response(Message.STATUS200, Message.REMINDER_SET, null);
		} else
			return new Response(Message.STATUS200, Message.REMINDER_NOT_SET, null);
	}

	/**
	 * @purpose : To edit Reminder
	 * @param   : token 
	 * @param   : noteid
	 * @param   : reminderdate
	 * @return
	 */
	 @Override
	public Response editReminder(String token, int noteid, String reminderdate) {
		 checkNote(noteid);
		List<Object> datelist = getDates(reminderdate);
		Date systemdate = (Date) datelist.get(0);
		Date userdate = (Date) datelist.get(1);
		if (userdate.after(systemdate)) {
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
			Note note = list.stream().filter(e -> e.getNoteId() == noteid).collect(Collectors.toList()).get(0);
			note.setReminder(systemdate.toString());
			noteRepository.save(list.get(0));
			return new Response(Message.STATUS200, Message.REMINDER_EDIT, null);
		}
		return new Response(Message.STATUS200, Message.REMINDER_NOT_SET, null);

	}

	/**
	 * @purpose : To delete Reminder
	 * @param : Token
	 * @param : Noteid
	 * @return : Response Type
	 */
	 @Override
	public Response deleteReminder(String token, int note_id, String reminderdate) {
		 checkNote(note_id);
		List<Object> datelist = getDates(reminderdate);
		Date systemdate = (Date) datelist.get(0);
		Date userdate = (Date) datelist.get(1);
		if (userdate.after(systemdate)) {
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());

			Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
			noteRepository.delete(note);
			return new Response(Message.STATUS200, Message.REMINDER_DELETE, note);
		}
		return new Response(Message.STATUS200, Message.REMINDER_NOT_DELETED, null);
	}

	/**
	 * @purpose : To collaborate note
	 * @param : Token
	 * @param : DTO -CollaboratorDto type
	 * @return : Response Type
	 */
	 @Override
	public Response collaborator(CollaboratorDto collaboratorDto, String token) {
		User user=userService.isUser(token);
		checkNote(collaboratorDto.getNoteid());
		User reciver=checkByUserByEmailId(collaboratorDto.getReciveremailid());
		Collaborator collaborator = mapper.map(collaboratorDto, Collaborator.class);
		collaborator.setReciverid(reciver.getId());
		collaborator.setSenderid(user.getId());
		collaboratorRepository.save(collaborator);
		return new Response(Message.STATUS200, Message.COLABORATOR_ADDED, null);
	}

	/**
	 * @purpose : To Get All Collaborated Notes
	 * @param : Token
	 * @return : Response Type
	 */
	 @Override
	public Response getCollaborated(String token ,int noteid) {
		User user = userService.isUser(token);
		checkNoteByUserId(user.getId());
		Collaborated collaboratedlist = new Collaborated();
		List<Collaborator> list = collaboratorRepository.findAllBySenderid(user.getId());    // get list of  collaborated row
	     list=     list.stream().filter(e-> e.getNoteid()==noteid).collect(Collectors.toList());
		if (list != null) {
			List<String> listofemails = new ArrayList<String>();
			for (Collaborator collaborator : list) {				                                
				listofemails.add(userRepository.findById(collaborator.getReciverid()).get().getEmail());   // list of reciever			 
			}
			List<String> emails = listofemails.stream().distinct().collect(Collectors.toList());          //remove douplicate
			collaboratedlist.setSender(user.getEmail());
			collaboratedlist.setReciever(emails);

			return new Response(Message.STATUS200, Message.COLLABORATE_LIST, collaboratedlist);
		}
		return new Response(Message.STATUS200, Message.COLLABORATE_LIST_IS_EMPTY, null);
	}

	/**
	 * @purpose : To check Note Present or not By User id
	 * @param : int
	 * @return : List of Note type
	 */

	public List<Note> checkNoteByUserId(int userid) {
		List<Note> list = null;
		try {
			list = noteRepository.findByUserId(userid);

		} catch (Exception e) {
			throw new CustomException.EmptyNoteList("note list is empty");
		}
		return list;
	}

	/**
	 * @purpose : To Check Note Present or not By Note id
	 * @param : int
	 * @return : Note Type
	 */
	public Note checkNote(int note_id) {
		Note note = null;
		try {
			Optional<Note> optionalnote = noteRepository.findById(note_id);
			note = optionalnote.get();

		} catch (Exception e) {
			throw new CustomException.EmptyNote("Invalid note id ");
		}
		return note;
	}

	/**
	 * @purpose : To give invalid not response
	 * @param : -
	 * @return : Response Type
	 */

	@SuppressWarnings("unused")

	/**
	 * @purpose : To give empty collaborator list response
	 * @param : -
	 * @return : Response Type
	 */
	private Response collaboratorEmptyListResponse() {
		return new Response(Message.STATUS404, Message.COLLABORATOR_NOT_PRESENT, null);
	}

	/**
	 * @purpose : To get all Collaborator
	 * @param 	: token
	 * @return  : Response Type
	 */

//	public Response getAllCollaborator(String token) {
//
//		List<Collaborator> collaboratelist = collaboratorRepository.findAllByUserid(userService.isUser(token).getId());
//		if (collaboratelist != null) {
//			return new Response(Message.STATUS200, Message.COLLABORATE_LIST, collaboratelist);
//		}
//		return new Response(Message.STATUS200, Message.COLLABORATE_LIST_IS_EMPTY, null);
//	}

	/**
	 * @purpose  : To get all notes labels
	 * @param    : note id
	 * @param    : token
	 * @return   : Response Type
	 */
	@Override
	public Response getAllNoteLabels(String token, int noteid) {
		userService.isUser(token);
		checkNote(noteid);
		List<UserLabel> list = new ArrayList<UserLabel>();
		List<NoteLabel> labelist = noteLabelRepository.findAllByNoteid(noteid);
		if (labelist != null) {
			for (NoteLabel noteLabel : labelist) {
				int id = noteLabel.getLabelid();
				Optional<UserLabel> userLabel = userlabelRepository.findById(id);
				list.add(userLabel.get());
			}

			return new Response(Message.STATUS200, Message.ALL_LABLE, list);
		}
		return new Response(Message.STATUS200, Message.LABEL_NOT_FOUND, list);
	}

	private String currentDate() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
	}

	/**
	 * @purpose 		: return system date and user date in dd-mm-yyyy format
	 * @param userdate	: store date  
	 * @return          : Object type list
	 */
	public List<Object> getDates(String userdate) {
		Date systemdate = null;
		Date reminderdate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
		try {
			systemdate = simpleDateFormat.parse(currentDate());
			System.out.println(systemdate);
			reminderdate = simpleDateFormat.parse(userdate);
			System.out.println(reminderdate);
		} catch (Exception e) {
			// invalideDateResponse();
		}
		List<Object> list = new ArrayList<Object>();
		list.add(systemdate);
		list.add(reminderdate);
		return list;
	}
	
	public User checkByUserByEmailId(String email)
	{
	   User user = userRepository.findByEmail(email);
	     if(user==null)
	     {
	    	 throw new CustomException.UserNotExistException("Invalid Email Id");
	     }
		return user;
	}
	

}
