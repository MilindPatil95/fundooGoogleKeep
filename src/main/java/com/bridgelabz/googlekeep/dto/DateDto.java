package com.bridgelabz.googlekeep.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class DateDto 
{@NotEmpty 
  private int noteid;
  @Pattern(regexp = "^([0-2][0-9]|(3)[0-1])(\\-)(((0)[0-9])|((1)[0-2]))(\\-)\\d{4}$", message = "Invalid Date, Date format should be dd/mm/yyyy")
  private String reminderdate;
public int getNoteid() {
	return noteid;
}
public void setNoteid(int noteid) {
	this.noteid = noteid;
}
public String getReminderdate() {
	return reminderdate;
}
public void setReminderdate(String reminderdate) {
	this.reminderdate = reminderdate;
}
}
