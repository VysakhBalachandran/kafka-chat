package com.endpoint.kafkamessaging.message.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.endpoint.kafkamessaging.cache.respository.CacheRepository;
import com.endpoint.kafkamessaging.auth.AuthService;
import com.endpoint.kafkamessaging.auth.controller.MessageRequest;
import com.endpoint.kafkamessaging.message.MessageService;
import com.endpoint.kafkamessaging.persistent.model.User;
import com.endpoint.kafkamessaging.persistent.repository.UserRepository;
import com.endpoint.kafkamessaging.util.StringHelper;

import java.util.UUID;


@RestController
@RequestMapping("/api/message")
public class MessageController {

     @Autowired
    MessageService messageService;
    
    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> sendMessage(@Valid @RequestBody MessageRequest mesageRequest) {
    	
     	messageService.sendMessage(mesageRequest.getAccessToken(), mesageRequest.getSendTo(), mesageRequest.getMsg());
        System.out.println("here iam");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
