package com.endpoint.kafkamessaging.message;

import java.util.List;

import com.endpoint.kafkamessaging.persistent.model.Message;

public interface MessageService {

    public void sendMessage(String accessToken, Long sendTo, String msg);

    List<Message> getMessageHistory(Long fromUserId, Long toUserId);
}
