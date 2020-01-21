package com.bridgelabz.googlekeep.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NoteLabel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int noteid;
	private int labelid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNoteid() {
		return noteid;
	}

	public void setNoteid(int noteid) {
		this.noteid = noteid;
	}

	public int getLabelid() {
		return labelid;
	}

	public void setLabelid(int labelid) {
		this.labelid = labelid;
	}
}
