package com.bridgelabz.googlekeep.model;

import java.util.List;

public class Collaborated 
{
  private String sender;
  private List<String> reciever;
public String getSender() {
	return sender;
}
public void setSender(String sender) {
	this.sender = sender;
}
public List<String> getReciever() {
	return reciever;
}
public void setReciever(List<String> reciever) {
	this.reciever = reciever;
}
}
