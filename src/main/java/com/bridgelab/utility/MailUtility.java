package com.bridgelab.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MailUtility {
	
	@Autowired
	JavaMailSender javaMailSender;

	
	public void accountVerification(String mailsender,String mailreceiver ,String subject, String Text) {
		try {
			System.out.println(javaMailSender);
			System.out.println("sending mail");
			createAndSend(mailsender, mailreceiver, subject, Text);
		
		}catch (Exception e) {
			System.out.println(e);
		}
	}
 void createAndSend(String mailsender,String mailreceiver ,String subject, String Text)
	    {
	        SimpleMailMessage mailMessage = new SimpleMailMessage();
	        
	        mailMessage.setTo(mailsender,mailreceiver);
	        
	        mailMessage.setSubject(subject);
	        
	        mailMessage.setText(Text);
	        
	        javaMailSender.send(mailMessage);
	        System.out.println("Mail Successfully Send");
	    }
}

