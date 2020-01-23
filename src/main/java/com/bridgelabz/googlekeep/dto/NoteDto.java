package com.bridgelabz.googlekeep.dto;

import javax.validation.constraints.NotEmpty;

public class NoteDto 
{     @NotEmpty(message = "Title Should not be empty") 
	  private String title;
      @NotEmpty(message = "Description should not be empty") 
	  private String desctiption;
	  @NotEmpty(message = "Color field shuld not be empty")
      private String color;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
