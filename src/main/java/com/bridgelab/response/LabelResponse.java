package com.bridgelab.response;

import java.util.List;
import com.bridgelab.model.UserLabel;

public class LabelResponse 
{ private int status;
  private String message;
  private List<UserLabel> label;
 
	public int getStatus() {
	return status;
}

public void setStatus(int status) {
	this.status = status;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public List<UserLabel> getLabel() {
	return label;
}

public void setLabel(List<UserLabel> label) {
	this.label = label;
}

	public LabelResponse(int status, String message, List<UserLabel>  label) {
	
	this.status = status;
    this.message = message;
	this.label = label;
}

	
}
