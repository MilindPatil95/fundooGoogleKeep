package com.bridgelabz.googlekeep.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Note
{ 
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int noteId;
  private String title;
  private String desctiption;
  private String reminder;
  private String color;
  private boolean isArchive;
  private boolean isPin;
  private boolean isTrash;
  private int userId;
  
 @Override
public String toString() {
	return "Note [noteId=" + noteId + ", title=" + title + ", desctiption=" + desctiption + ", reminder=" + reminder
			+ ", color=" + color + ", isArchive=" + isArchive + ", isPin=" + isPin + ", isTrash=" + isTrash
			+ ", userId=" + userId + "]";
}
public boolean isArchive() {
	return isArchive;
}
public void setArchive(boolean isArchive) {
	this.isArchive = isArchive;
}
public boolean isPin() {
	return isPin;
}
public int getNoteId() {
	return noteId;
}
public void setNoteId(int noteId) {
	this.noteId = noteId;
}
public int getUserId() {
	return userId;
}
public void setUserId(int userId) {
	this.userId = userId;
}
public void setPin(boolean isPin) {
	this.isPin = isPin;
}
public boolean isTrash() {
	return isTrash;
}
public void setTrash(boolean isTrash) {
	this.isTrash = isTrash;
}

 
public String getReminder() {
	return reminder;
}
public void setReminder(String reminder) {
	this.reminder = reminder;
}
public String getColor() {
	return color;
}
public void setColor(String color) {
	this.color = color;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDesctiption() {
	return desctiption;
}
public void setDesctiption(String desctiption) {
	this.desctiption = desctiption;
}
}
