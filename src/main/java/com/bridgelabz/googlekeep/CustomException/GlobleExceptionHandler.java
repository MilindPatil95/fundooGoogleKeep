package com.bridgelabz.googlekeep.CustomException;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bridgelabz.googlekeep.response.Response;
import com.bridgelabz.googlekeep.utility.Message;

@Component
@RestControllerAdvice
public class GlobleExceptionHandler 
{   
	@ExceptionHandler(CustomException.InvalidTokenException.class)
	public Response invalidTokenResponse(CustomException.InvalidTokenException e)
	{
		return new Response(Message.STATUS200,e.getMessage(),null);
	}
	
	@ExceptionHandler(CustomException.TokenExpiredException.class)
	public Response tokenExpireResponse(CustomException.TokenExpiredException e)
	{
		return new Response(Message.STATUS200,e.getMessage(), null);
	}
	@ExceptionHandler(CustomException.InvalidNoteException.class)
	public Response InvalidNoteResponse(CustomException.InvalidNoteException e)
	{
		return new Response(Message.STATUS200,e.getMessage(), null);
	}
	
	@ExceptionHandler(CustomException.EmptyNoteListException.class)
	public Response noteListIsEmptyResponse(CustomException.EmptyNoteListException e)
	{
		return new Response(Message.STATUS200, e.getMessage(), null);
	}
	@ExceptionHandler(CustomException.InvaidLabelException.class)
	public Response InvaidLabelResponse()
	{
		return new Response(Message.STATUS200, Message.LABEL_NOT_FOUND, null);
	}
	@ExceptionHandler(CustomException.EmptyLabelListException.class)
	public Response InvalidLabelListResponse()
	{
		return new Response(Message.STATUS200, Message.LABELS_NOT_FOUND, null);
	}
	@ExceptionHandler(CustomException.UserNotExistException.class)
	public Response userNotFoundResponse()
	{
	  return new Response(Message.STATUS200, Message.USER_NOT_EXIST,null);	
			  
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Response methodArgumentNotValidException(MethodArgumentNotValidException e)
	{
		 return new Response(Message.STATUS200,e.getBindingResult().getFieldError().getDefaultMessage(), null);
	}
	@ExceptionHandler(CustomException.ProfileNotSave.class)
	public Response profileNotSaveResponse() {
            return new Response(Message.STATUS200,Message.PROFILE_NOT_SAVE, null);
	}
	
}
  
	

