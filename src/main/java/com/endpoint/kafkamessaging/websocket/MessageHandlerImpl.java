package com.endpoint.kafkamessaging.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class MessageHandlerImpl implements MessageHandler {

    @Override
    public void addSessionToPool(Long userId, WebSocketSession session) {
try {
        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);
//System.out.println("addSessionToPool-userSessions--"+userSessions.size());
        if (userSessions != null) {
        	System.out.println("-userSessions now--"+userSessions.size());
            userSessions.add(session);
            WebSocketPool.websockets.put(userId, userSessions);
        } else {
        	
        	 
            Set<WebSocketSession> newUserSessions = new HashSet<>();
            System .out.println("userId from session settings--"+userId);
            newUserSessions.add(session);
            System.out.println("snew ession--"+newUserSessions);
            WebSocketPool.websockets.put(userId, newUserSessions);
            System.out.println("session after setting --"+ WebSocketPool.websockets.get(userId));
            
        }

    
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
}
    @Override
    public void sendMessageToUser(Long userId, String message) throws IOException {
try {
        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);
System.out.println("userSessions--"+userSessions.size());
        if (userSessions == null) {
            return;
        }

        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : userSessions) {
            session.sendMessage(textMessage);
        }
}
catch(Exception e)
{
	e.printStackTrace();
}
    }

    @Override
    public void removeFromSessionToPool(Long userId, WebSocketSession session) {
        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions != null) {
            for (WebSocketSession sessionItem : userSessions) {
                if (sessionItem.equals(session)) {
                    userSessions.remove(session);
                }
            }
        }
        WebSocketPool.websockets.put(userId, userSessions);
    }
}
