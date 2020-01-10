package com.bridgelab.response;

import java.util.List;

import com.bridgelab.model.User;

public class Response {
	private int status;
	private String message;
	
	private List<User> users;
	
	
	public Response(int status, String message, List<User> users) {

		this.status = status;
		this.message = message;
		this.users = users;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUser_added() {
		return message;
	}

	public void setUser_added(String message) {
		this.message = message;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}


}
