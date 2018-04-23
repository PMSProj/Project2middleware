package com.Controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.model.Chat;

@Controller
public class SockController {
	//to send data to the client
	private static final Log logger=LogFactory.getLog(SockController.class);
    private final SimpMessagingTemplate messagingTemplate;
	//list of username's who joined the chat room
    private List<String> users=new ArrayList<String>();
   @Autowired
   public SockController(SimpMessagingTemplate messagingTemplate){
//	super();
	this.messagingTemplate=messagingTemplate;
}
@SubscribeMapping("/join/{username}")
public List<String> join(@DestinationVariable String username){
	System.out.println("Newly joined username is:"+username);
	if(!users.contains(username))
		users.add(username);
	messagingTemplate.convertAndSend("/topic/join",username);
	return users;
}

@MessageMapping(value="/chat")
public void chatReveived(Chat chat){  //to ,from ,message  Recieved 
	//group chat
	if(chat.getTo().equals("all")){
		System.out.println("IN CHAT REVEIVED"+chat.getMessage()+""+chat.getFrom()+"to"+chat.getTo());
		messagingTemplate.convertAndSend("/queue/chats",chat);
	}
	else{
		System.out.println("CHAT TO"+chat.getTo()+"From"+chat.getFrom()+"Message"+chat.getMessage());
		messagingTemplate.convertAndSend("/queue/chats/"+chat.getTo(),chat);
		messagingTemplate.convertAndSend("/queue/chats/"+chat.getFrom(),chat);
	}
}

}

