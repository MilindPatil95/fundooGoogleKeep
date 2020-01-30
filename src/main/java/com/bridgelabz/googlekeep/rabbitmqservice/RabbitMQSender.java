package com.bridgelabz.googlekeep.rabbitmqservice;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bridgelabz.googlekeep.model.DataContainer;
@Service
public class RabbitMQSender {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${exchange}")
	private String exchange;
	
	@Value("${routingkey}")
	private String routingkey;	
	
	public void send(DataContainer dataobj) {
	  
		rabbitTemplate.convertAndSend(exchange, routingkey, dataobj);	
		
	}
} 