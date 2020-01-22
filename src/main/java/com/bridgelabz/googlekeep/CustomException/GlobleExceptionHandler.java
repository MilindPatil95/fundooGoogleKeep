package com.bridgelabz.googlekeep.CustomException;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.utility.Message;

@Component
@RestControllerAdvice
public class GlobleExceptionHandler 
{   
	@ExceptionHandler(CustomException.InvalidToken.class)
	public Response invalidTokenResponse()
	{
		return new Response(Message.STATUS200,Message.INVALID_TOKEN,null);
	}
	
	@ExceptionHandler(CustomException.TokenExpired.class)
	public Response tokenExpireResponse()
	{
		return new Response(Message.STATUS200,Message.TOKEN_EXPIRED, null);
	}
	
	@ExceptionHandler(CustomException.EmptyNoteList.class)
	public Response noteListIsEmpty()
	{
		return new Response(Message.STATUS200, Message.NOTES_NOT_PRESENT, null);
	}
	
	@ExceptionHandler(CustomException.EmptyNote.class)
	public Response InvalidNoteResponse()
	{
		return new Response(Message.STATUS200, Message.NOTES_NOT_PRESENT, null);
	}
	@ExceptionHandler(CustomException.UserNotExistException.class)
	public Response userNotFound()
	{
	  return new Response(Message.STATUS200, Message.USER_NOT_EXIST,null);	
			  
	}
	@ExceptionHandler(CustomException.InvalidLabelId.class)
	public Response invalidToken()
	{
		return new Response(Message.STATUS200, Message.INVALID_LABEL, null);
	}
}
  
	

