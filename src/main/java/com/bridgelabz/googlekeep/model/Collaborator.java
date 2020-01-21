package com.bridgelabz.googlekeep.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Collaborator {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int noteid;
	private int senderid;
	private int reciverid;
	private boolean pin;
	private boolean archive;
  

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSenderid() {
		return senderid;
	}

	public void setSenderid(int senderid) {
		this.senderid = senderid;
	}

	public int getReciverid() {
		return reciverid;
	}

	public void setReciverid(int reciverid) {
		this.reciverid = reciverid;
	}

	public int getNoteid() {
		return noteid;
	}

	public void setNoteid(int noteid) {
		this.noteid = noteid;
	}

	
	public boolean isPin() {
		return pin;
	}

	public void setPin(boolean pin) {
		this.pin = pin;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	

}
