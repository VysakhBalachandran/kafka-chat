package com.endpoint.kafkamessaging.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.endpoint.kafkamessaging.auth.AuthService;
import com.endpoint.kafkamessaging.persistent.model.User;
import com.endpoint.kafkamessaging.persistent.repository.UserRepository;
import com.endpoint.kafkamessaging.util.StringHelper;
import com.endpoint.kafkamessaging.cache.respository.CacheRepository;

import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    CacheRepository cacheRepository;

    @GetMapping("/getAppStatus")
    public ResponseEntity<Object> getAppRunningStatus(){
        return ResponseEntity.ok("Welcome to chat application using kafka !!!");
    }
    
    @RequestMapping(value = "/getcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getCode(@Valid @RequestBody ActivationRequest activationRequest) {
    	
    	int code = StringHelper.generateRandomNumber(6);
    	
    	// save the activation code to the cache repository (cached auth token)
    	//cacheRepository.putActivationCode(activationRequest.getMobile(), String.valueOf(code));

    	ActivationResponse activationResponse = ActivationResponse.builder()
                .mobile(activationRequest.getMobile())
                .activationCode(String.valueOf(code))
                .build();

        return new ResponseEntity<>(
                activationResponse,
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        //String mobile = cacheRepository.queryMobileActivationCode(loginRequest.getMobile(), loginRequest.getActivationCode());

        String mobile=loginRequest.getMobile();
        if(mobile == null) {
            return new ResponseEntity<>(
                    "Mobile number not found!",
                    HttpStatus.NOT_FOUND);
        } else {
            Long userId = 0L;
            User user = userRepository.findByMobile(loginRequest.getMobile());
            if(user == null) {
                // save user in persistence
                userRepository.save(
                        User.builder()
                        .mobile(loginRequest.getMobile())
                        .build()
                );
                user = userRepository.findByMobile(loginRequest.getMobile());
            }
            userId = user.getUserId();
            String accessToken = UUID.randomUUID().toString();
            authService.putAccessToken(accessToken, userId);

            return new ResponseEntity<>(
                    LoginResponse.builder()
                            .accessToken(accessToken)
                            .build(),
                    HttpStatus.OK);
        }
    }



}
