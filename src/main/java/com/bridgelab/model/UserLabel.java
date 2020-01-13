package com.bridgelab.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserLabel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int label_id;
	private String labelname;
	private int userId;

	public int getLabel_id() {
		return label_id;
	}

	public void setLabel_id(int label_id) {
		this.label_id = label_id;
	}

	public String getLabel() {
		return labelname;
	}

	public void setLabel(String label) {
		this.labelname = label;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
