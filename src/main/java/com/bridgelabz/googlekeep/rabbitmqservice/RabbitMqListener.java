package com.bridgelabz.googlekeep.rabbitmqservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bridgelabz.googlekeep.model.DataContainer;
import com.bridgelabz.googlekeep.utility.MailUtility;
import com.google.gson.Gson;

@Component
public class RabbitMqListener {
	@Autowired
	MailUtility mailUtility=new MailUtility();
	@RabbitListener(queues = "milind")
	public void listen(byte[] message) {
		String msg = new String(message);
		DataContainer dataobj = new Gson().fromJson(msg, DataContainer.class);
		String mailsender = "forgotbridge70@gmail.com";
		System.out.println("mail sending......in....progressssssssssss");
		mailUtility.accountVerification(mailsender, dataobj.getEmailId(),"testing",dataobj.getMassage()); // send m
		System.out.println("send successfully");
	}
}
