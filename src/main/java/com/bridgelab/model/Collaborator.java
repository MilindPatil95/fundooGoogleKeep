package com.bridgelab.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Collaborator {
	@Id
	private int collaborator_id;
	private String sender_email_id;
	private String reciever_email_id;
	private int note_id;

	public int getNote_id() {
		return note_id;
	}

	public void setNote_id(int note_id) {
		this.note_id = note_id;
	}

	public int getCollaborator_id() {
		return collaborator_id;
	}

	public void setCollaborator_id(int collaborator_id) {
		this.collaborator_id = collaborator_id;
	}

	public String getSender_email_id() {
		return sender_email_id;
	}

	public void setSender_email_id(String sender_email_id) {
		this.sender_email_id = sender_email_id;
	}

	public String getReciever_email_id() {
		return reciever_email_id;
	}

	public void setReciever_email_id(String reciever_email_id) {
		this.reciever_email_id = reciever_email_id;
	}
}
