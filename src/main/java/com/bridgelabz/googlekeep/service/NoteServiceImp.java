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
import com.bridgelabz.googlekeep.dto.DateDto;
import com.bridgelabz.googlekeep.dto.NoteDto;
import com.bridgelabz.googlekeep.model.Collaborated;
import com.bridgelabz.googlekeep.model.Collaborator;
import com.bridgelabz.googlekeep.model.Note;
import com.bridgelabz.googlekeep.model.NoteLabel;
import com.bridgelabz.googlekeep.model.SotedNote;
import com.bridgelabz.googlekeep.model.User;
import com.bridgelabz.googlekeep.model.UserLabel;
import com.bridgelabz.googlekeep.repository.CollaboratorRepository;
import com.bridgelabz.googlekeep.repository.LabelRepository;
import com.bridgelabz.googlekeep.repository.NoteLabelRepository;
import com.bridgelabz.googlekeep.repository.NoteRepository;
import com.bridgelabz.googlekeep.repository.RedisRepositoryImpl;
import com.bridgelabz.googlekeep.repository.UserRepository;
import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.utility.JwtUtil;
import com.bridgelabz.googlekeep.utility.Message;

@Service
public class NoteServiceImp implements INoteService {
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
	@Autowired
	RedisRepositoryImpl redisRepositoryImpl;
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
		if (redisRepositoryImpl.findUser(token) != null) {                    //Check login user login or not
			Note note = mapper.map(notedto, Note.class);                         
			List<Note> list = new ArrayList<Note>();                     
			User user = userService.isUser(token);                         
			note.setUserId(user.getId());                                           //set and add note in repository
			noteRepository.save(note);
			list.add(note);
			return new Response(Message.STATUS200, Message.ADD_NOTE, list);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);
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
		if (redisRepositoryImpl.findUser(token) != null) {                            //Check login user login or not
			Note note = checkNoteByNoteId(note_id);                                   //check note present or not
			userService.isUser(token);
			if (note.isTrash()) {
				noteRepository.delete(note);                                          //remove from repository
				return new Response(Message.STATUS200, Message.DTELETE_NOTE, null);
			}
			return new Response(Message.STATUS404, Message.NOTE_NOT_IN_TRASH, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose :To get all notes
	 * @param :token
	 * @param :note_id (int type)
	 * @param :DTO     - notedto store cedential data
	 * @return :Respose type
	 */
	@Override
	public Response getAllNotes(String token) {
		if (redisRepositoryImpl.findUser(token) != null) {                               //Check login user login or not
			User user = userService.isUser(token);														
			List<Note> list = new ArrayList<Note>();
			List<SotedNote> obj = new ArrayList<>();
			list = noteRepository.findByUserId(user.getId());
			checkNoteList(list);
			for (Note note : list) {
				SotedNote objSotedNote = new SotedNote();
				if (note.isArchive() == false) {                   
					if (note.isTrash() == false) {                                                         //  set note type
						objSotedNote.setNote(note);                                          
						@SuppressWarnings("unchecked")
						List<UserLabel> list1 = (List<UserLabel>) getAllNoteLabels(token, note.getNoteId()).getObj(); 
						objSotedNote.setList(list1);                                                                   // add UserLabel
						List<Collaborator> collboratorlist = collaboratorRepository.findAllByNoteid(note.getNoteId());
						objSotedNote.setCollboratorlist(collboratorlist);
						obj.add(objSotedNote);                                                                        // add collaborator type list
					}
				}
			}
			return new Response(Message.STATUS200, Message.NOTES_RETURN, obj);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	public void checkNoteList(List<Note> list) {
		if (list.isEmpty()) {
			throw new CustomException.EmptyNoteListException("note list is empty");
		}
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
		if (redisRepositoryImpl.findUser(token) != null) {                                     //Check login user login or not
			checkNoteByNoteId(note_id);                                                        //check note present or not
			Note note = mapper.map(notedto, Note.class);
			userService.isUser(token);                                                         //check user valid or not
			noteRepository.save(note);                                                         // add note in repository
			return new Response(Message.STATUS200, Message.NOTE_UPDATED, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose :pin and unpin to note
	 * @param :token
	 * @param :note_id (int type)
	 * @return :Respose type
	 */
	@Override
	public Response pin(String token, int note_id) {
		if (redisRepositoryImpl.findUser(token) != null) {                                    //Check login user login or not
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());          
			checkNoteList(list);                                                              //check list empty or not
			checkNoteByNoteId(note_id);
			List<Collaborator> collaboratorlist = collaboratorRepository.findAllByNoteid(note_id);
			if (collaboratorlist.isEmpty() == false) {
				for (Collaborator collaborator : collaboratorlist) {
					if (collaborator.isPin()) {                                             
						collaborator.setPin(false);                                          
						collaboratorRepository.save(collaborator);                            // add collaborator repository
						return new Response(Message.STATUS200, Message.UNPING, null);
					} else {
						collaborator.setPin(true);
						collaboratorRepository.save(collaborator);
						return new Response(Message.STATUS200, Message.PING, null);
					}
				}
			} else {
				Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);       // find note in list
                 
				if (note.isPin()) {
					note.setPin(false);
					noteRepository.save(note);
					return new Response(Message.STATUS200, Message.UNPING, null);
				} else {
					note.setPin(true);                                                                                 
					noteRepository.save(note);                                                                    // add note repository
					return new Response(Message.STATUS200, Message.PING, null);
				}
			}
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}
	/**
	 * @purpose :archive and archive to note
	 * @param :token
	 * @param :note_id
	 * @return :Respose type
	 */
	@Override
	public Response archive(String token, int note_id) {
		if (redisRepositoryImpl.findUser(token) != null) {                                      //Check login user login or not
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
			checkNoteByNoteId(note_id);                                                         //check note present or not
			List<Collaborator> collaboratorlist = collaboratorRepository.findAllByNoteid(note_id);
			if (collaboratorlist.isEmpty() == false) {
				for (Collaborator collaborator : collaboratorlist) {
					if (collaborator.isArchive()) {
						collaborator.setArchive(false);
						collaboratorRepository.save(collaborator);                            //add collaborator
						return new Response(Message.STATUS200, Message.UNARCHIVE, null);
					} else {
						collaborator.setArchive(true);
						collaboratorRepository.save(collaborator);							//add collaborator 
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
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);
	}

	/**
	 * @purpose : To get all archive notes
	 * @param :token
	 * @return :Respose type
	 */
	@Override
	public Response getAllArchive(String token) {
		if (redisRepositoryImpl.findUser(token) != null) {                                                  //Check login user login or not
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
			checkNoteList(list);																			   //check list empty or not
			List<Note> list1 = list.stream().filter(e -> e.isArchive() == true).collect(Collectors.toList());
			return new Response(Message.STATUS200, Message.ALL_ARCHIVE, list1);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : archive and archive to note
	 * @param : token
	 * @param : note_id
	 * @return : Respose type
	 */
	@Override
	public Response getAllTrash(String token) {
		if (redisRepositoryImpl.findUser(token) != null) {                                               //Check login user login or not
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());                       
			checkNoteList(list);                                                                          //check list present or not
			List<Note> list1 = list.stream().filter(e -> e.isTrash() == true).collect(Collectors.toList());
			return new Response(Message.STATUS200, Message.ALL_TRASH, list1);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To trash perticular note
	 * @param : token
	 * @param : note_id
	 * @return : Respose type
	 */
	@Override
	public Response trash(String token, int note_id) {
		if (redisRepositoryImpl.findUser(token) != null) {                                             //Check login user login or not
			checkNoteByNoteId(note_id);                                                                 //Check not by id
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
			checkNoteList(list);                                                                       //check list empty or not
			Note note = list.stream().filter(e -> e.getNoteId() == note_id).collect(Collectors.toList()).get(0);
			if (note.isTrash()) {
				note.setTrash(false);
				noteRepository.save(note);                                                             //add note in repository                         
				return new Response(Message.STATUS200, Message.UNTRASH, null);
			} else {                                                       
				note.setTrash(true);                                    
				noteRepository.save(note);                                                           //add note in repository
				return new Response(Message.STATUS200, Message.TRASH, null);
			}
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To set Reminder
	 * @param : token
	 * @param : note_id
	 * @return : Respose type
	 */
	@Override
	public Response reminder(DateDto datedto, String token) {
		if (redisRepositoryImpl.findUser(token) != null) {                                                   //Check login user login or not
			List<Object> datelist = getDates(datedto.getReminderdate());                                    
			userService.isUser(token);                                                                       // Chek user
			checkNoteByNoteId(datedto.getNoteid());
			Optional<Note> cheknote = noteRepository.findById(datedto.getNoteid());
			if (cheknote.get().getReminder() == null) {
				checkNoteByNoteId(datedto.getNoteid());
				Date systemdate = (Date) datelist.get(0);
				Date userdate = (Date) datelist.get(1);
				if (userdate.after(systemdate)) {
					Note note = new Note();
					note.setNoteId(datedto.getNoteid());
					note.setReminder(systemdate.toString());
					noteRepository.save(note);
					return new Response(Message.STATUS200, Message.REMINDER_SET, null);
				} else
					return new Response(Message.STATUS200, Message.REMINDER_NOT_SET, null);
			} else
				return new Response(Message.STATUS200, Message.REMINDER_SET_ALLREADY, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To edit Reminder
	 * @param : token
	 * @param : noteid
	 * @param : reminderdate
	 * @return
	 */
	@Override
	public Response editReminder(DateDto datedto, String token) {
		if (redisRepositoryImpl.findUser(token) != null) {
			List<Object> datelist = getDates(datedto.getReminderdate());
			userService.isUser(token);
			checkNoteByNoteId(datedto.getNoteid());
			Date systemdate = (Date) datelist.get(0);
			Date userdate = (Date) datelist.get(1);
			if (userdate.after(systemdate)) {
				Note note = new Note();
				note.setNoteId(datedto.getNoteid());
				note.setReminder(datedto.getReminderdate());
				noteRepository.save(note);
				return new Response(Message.STATUS200, Message.REMINDER_SET, null);
			} else
				return new Response(Message.STATUS200, Message.REMINDER_NOT_SET, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To delete Reminder
	 * @param : Token
	 * @param : Noteid
	 * @return : Response Type
	 */
	@Override
	public Response deleteReminder(int noteid, String token) {
		if (redisRepositoryImpl.findUser(token) != null) {
			userService.isUser(token);
			checkNoteByNoteId(noteid);
			List<Note> list = checkNoteByUserId(userService.isUser(token).getId());
			checkNoteList(list);
			Note note = list.stream().filter(e -> e.getNoteId() == noteid).collect(Collectors.toList()).get(0);
			note.setReminder(null);
			noteRepository.save(note);
			return new Response(Message.STATUS200, Message.REMINDER_DELETE, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To collaborate note
	 * @param : Token
	 * @param : DTO -CollaboratorDto type
	 * @return : Response Type
	 */
	@Override
	public Response collaborator(CollaboratorDto collaboratorDto, String token) {
		if (redisRepositoryImpl.findUser(token) != null) {
			User user = userService.isUser(token);
			checkNoteByNoteId(collaboratorDto.getNoteid());
			User reciver = checkByUserByEmailId(collaboratorDto.getReciveremailid());
			Collaborator collaborator = mapper.map(collaboratorDto, Collaborator.class);
			collaborator.setReciverid(reciver.getId());
			collaborator.setSenderid(user.getId());
			collaboratorRepository.save(collaborator);
			return new Response(Message.STATUS200, Message.COLABORATOR_ADDED, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To Get All Collaborated Notes
	 * @param : Token
	 * @return : Response Type
	 */
	@Override
	public Response getCollaborated(String token, int noteid) {
		if (redisRepositoryImpl.findUser(token) != null) {

			User user = userService.isUser(token);
			checkNoteByNoteId(noteid);
			checkNoteByUserId(user.getId());
			Collaborated collaboratedlist = new Collaborated();
			List<Collaborator> list = collaboratorRepository.findAllBySenderid(user.getId()); // get list of
																								// collaborated
			if (list != null) {																					// row
		     	list = list.stream().filter(e -> e.getNoteid() == noteid).collect(Collectors.toList());			
				List<String> listofemails = new ArrayList<String>();
				for (Collaborator collaborator : list) {
					listofemails.add(userRepository.findById(collaborator.getReciverid()).get().getEmail()); // list of
																												// reciever
				}
				List<String> emails = listofemails.stream().distinct().collect(Collectors.toList()); // remove
																										// douplicate
				collaboratedlist.setSender(user.getEmail());
				collaboratedlist.setReciever(emails);

				return new Response(Message.STATUS200, Message.COLLABORATE_LIST, collaboratedlist);
			}
			return new Response(Message.STATUS200, Message.COLLABORATE_LIST_IS_EMPTY, null);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To get all notes labels
	 * @param : note id
	 * @param : token
	 * @return : Response Type
	 */
	@Override
	public Response getAllNoteLabels(String token, int noteid) {
		if (redisRepositoryImpl.findUser(token) != null) {

			userService.isUser(token);
			checkNoteByNoteId(noteid);
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
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);
	}

	/**
	 * @purpose : To get current date in dd-MM-yyyy format
	 * @return : String
	 */
	private String currentDate() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
	}

	/**
	 * @purpose : return system date and user date in dd-mm-yyyy format
	 * @param userdate : store date
	 * @return : Object type list
	 */
	public List<Object> getDates(String userdate) {
		Date systemdate = null;
		Date reminderdate = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
			systemdate = simpleDateFormat.parse(currentDate());
			System.out.println(systemdate);
			reminderdate = simpleDateFormat.parse(userdate);
		} catch (Exception e) {

		}
		System.out.println(reminderdate);
		List<Object> list = new ArrayList<Object>();
		list.add(systemdate);
		list.add(reminderdate);
		return list;
	}

	/**
	 * @purpose :To check User By Email id
	 * @param email : string tyope
	 * @return : User type
	 */
	public User checkByUserByEmailId(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new CustomException.UserNotExistException("Invalid Email Id");
		}
		return user;
	}

	/**
	 * @purpose :To sort notes by title
	 * @param token : String type
	 * @return :
	 */
	@SuppressWarnings("unchecked")
	public Response sortByTitle(String token) {
		if (redisRepositoryImpl.findUser(token) != null) {
			userService.isUser(token);
			List<SotedNote> list = (List<SotedNote>) getAllNotes(token).getObj();
			try {
				list = list.stream()
						.sorted((list1, list2) -> list1.getNote().getTitle().compareTo(list2.getNote().getTitle()))
						.collect(Collectors.toList());
			} catch (Exception e) {
				throw new CustomException.EmptyNoteListException("notes list is empty");
			}
			return new Response(Message.STATUS200, Message.SORTED_NOTE, list);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To sort notes by description
	 * @param token : String type
	 * @return : Response type
	 */

	@SuppressWarnings("unchecked")
	public Response sortByDscription(String token) {
		if (redisRepositoryImpl.findUser(token) != null) {
			userService.isUser(token);
			List<SotedNote> list = (List<SotedNote>) getAllNotes(token).getObj();			
			if (list.isEmpty()) {
				throw new CustomException.EmptyNoteListException("notes list is empty");
			}
			List<SotedNote> list3 = list.stream().sorted((list1, list2) -> list1.getNote().getDesctiption().compareTo(list2.getNote().getDesctiption())).collect(Collectors.toList());
			return new Response(Message.STATUS200, Message.SORTED_NOTE, list3);
		}
		return new Response(Message.STATUS200, Message.USER_NOT_LOGIN, null);

	}

	/**
	 * @purpose : To check Note Present or not By User id
	 * @param : int
	 * @return : List of Note type
	 */

	public List<Note> checkNoteByUserId(int userid) {

		List<Note> list = noteRepository.findByUserId(userid);
		jwtUtil.checkNoteList(list);
		return list;
	}

	/**
	 * @purpose : To Check Note Present or not By Note id
	 * @param : int
	 * @return : Note Type
	 */
	public Note checkNoteByNoteId(int note_id) {
		Note note = noteRepository.findById(note_id).orElseThrow(() -> new CustomException.InvalidNoteException("invalid note id"));
		return note;
	}

}
