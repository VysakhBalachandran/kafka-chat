package com.endpoint.kafkamessaging.message.broker;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.endpoint.kafkamessaging.message.MessageService;
import com.endpoint.kafkamessaging.websocket.MessageHandler;
import com.endpoint.kafkamessaging.websocket.WebSocketPool;

@Service
public class MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    MessageService messageService;

    @Autowired
    MessageHandler messageHandler;

    @KafkaListener(topics = "SEND_MESSAGE")
    public void messagesSendToUser(@Payload String message, @Headers MessageHeaders headers) {

    	System.out.println("--message--"+message);
        JSONObject jsonObject = new JSONObject(message);
        Long sendTo = Long.parseLong(jsonObject.getString("sendTo"));
        if (WebSocketPool.websockets.get(sendTo) != null) {

            String accessToken = jsonObject.getString("accessToken");
             String msg = jsonObject.getString("msg");

            messageService.sendMessage(accessToken, sendTo, msg);

        }
    }

}