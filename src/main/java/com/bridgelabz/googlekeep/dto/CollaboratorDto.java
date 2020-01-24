package com.bridgelabz.googlekeep.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class CollaboratorDto {
	@NotEmpty(message = "note id field should not be empty")
	private int noteid;
	@Pattern(regexp = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$", message = "Invalid email id")
	private String  reciveremailid;

	public String getReciveremailid() {
		return reciveremailid;
	}

	public void setReciveremailid(String reciveremailid) {
		this.reciveremailid = reciveremailid;
	}

	public int getNoteid() {
		return noteid;
	}

	public void setNoteid(int noteid) {
		this.noteid = noteid;
	}


}
