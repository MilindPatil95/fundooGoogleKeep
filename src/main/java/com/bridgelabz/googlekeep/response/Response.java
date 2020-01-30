package com.bridgelabz.googlekeep.response;
public class Response {
	private int status;
	private String message;
	
	private Object obj;
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Response(int status, String message, Object obj) {

		this.status = status;
		this.message = message;
		this.obj = obj;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	

}
