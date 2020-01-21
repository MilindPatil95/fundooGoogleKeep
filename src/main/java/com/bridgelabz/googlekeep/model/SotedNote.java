package com.bridgelabz.googlekeep.model;

import java.util.List;

public class SotedNote {
	private Note note;
	private List<UserLabel> labellist;
	private List<Collaborator> collboratorlist;
	


	public void setNote(Note note) {
		 this.note = note;
	   
	}
	public Note getNote() {
		 return this.note;
	   
	}
	public List<UserLabel> getList() {
		return labellist;
	}

	public List<UserLabel> setList(List<UserLabel> list) {
		return this.labellist = list;
	}

	public List<Collaborator> getCollboratorlist() {
		return collboratorlist;
	}

	public void setCollboratorlist(List<Collaborator> collboratorlist) {
		this.collboratorlist = collboratorlist;
	}
}
