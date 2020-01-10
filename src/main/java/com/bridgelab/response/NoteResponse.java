package com.bridgelab.response;

import java.util.List;

import com.bridgelab.model.Note;

public class NoteResponse {
	private int status;
	private String message;
	private List<Note> list;

	public NoteResponse(int status, String message, List<Note> list) {
		super();
		this.status = status;
		this.message = message;
		this.list = list;
	}

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

	public List<Note> getList() {
		return list;
	}

	public void setList(List<Note> list) {
		this.list = list;
	}
}
