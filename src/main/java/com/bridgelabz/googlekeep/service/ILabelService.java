package com.bridgelabz.googlekeep.service;
import com.bridgelabz.googlekeep.response.Response;

public interface ILabelService {
	public Response addLabelToNote(int noteid, int labelid, String token) ;
	public Response createLabel(String name, String token);
	public Response deleteLabel(String token, int id);
	public Response editLabel(String token, int id, String name);
	public Response getUserLabel(String token);
	
}
