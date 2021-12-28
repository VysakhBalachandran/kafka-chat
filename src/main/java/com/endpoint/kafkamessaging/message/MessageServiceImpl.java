package com.endpoint.kafkamessaging.message;

import com.endpoint.kafkamessaging.cache.respository.CacheRepository;
import com.endpoint.kafkamessaging.persistent.model.Message;
import com.endpoint.kafkamessaging.persistent.model.User;
import com.endpoint.kafkamessaging.persistent.repository.MessageRepository;
import com.endpoint.kafkamessaging.persistent.repository.UserRepository;
import com.endpoint.kafkamessaging.websocket.MessageHandler;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CacheRepository cacheRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageHandler messageHandler;

    @Override
    public void sendMessage(String accessToken, Long sendTo, String msg) {

        Long senderUserId = 0L;
        String senderId = null;//cacheRepository.getUserIdByAccessToken(accessToken);
System.out.println("sendMessage--"+accessToken);
System.out.println("sendTo--"+sendTo);

        if(senderId == null) {
            User sender = userRepository.findByToken(accessToken);
            if(sender != null) {
                senderUserId = sender.getUserId();
            }
        } else {
            senderUserId = Long.valueOf(senderId);
        }
        if (senderUserId == 0L) {
            return;
        }

        try {
            // enrich message with senderId
            JSONObject msgJson = new JSONObject();
            msgJson.put("msg", msg);
            msgJson.put("senderId", senderUserId);
            System.out.println("befor sent");
            messageHandler.sendMessageToUser(sendTo, msgJson.toString());
            Message message=new Message();
            message.setFromUserId(senderUserId);
            message.setMessage(msg);
            message.setToUserId(sendTo);
            Date localDate = new Date();
            message.setSentAt(localDate);
            message.setTopic("SEND_MESSAGE");
            storeMessageToUser(message);
        } catch (IOException e) {
        	e.printStackTrace();
            return;
        }
    }

    @Override
    public List<Message> getMessageHistory(Long fromUserId, Long toUserId) {
        return messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
    }

    private void storeMessageToUser(Message message) {

        messageRepository.save(message);

    }
}
