package com.endpoint.kafkamessaging.auth;

import com.endpoint.kafkamessaging.cache.respository.CacheRepository;
import com.endpoint.kafkamessaging.persistent.model.AccessToken;
import com.endpoint.kafkamessaging.persistent.model.User;
import com.endpoint.kafkamessaging.persistent.repository.AccessTokenRepository;
import com.endpoint.kafkamessaging.persistent.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    CacheRepository cacheRepository;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void putAccessToken(String token, Long userId) {

        // store token in the cache
       // cacheRepository.putAccessToken(token, String.valueOf(userId));

        // store token in the persistence
        AccessToken accessToken = AccessToken.builder()
        							.token(token)
        							.userId(userId)
        							.createdAt(Calendar.getInstance().getTime())
        							.build();
        accessTokenRepository.save(accessToken);
    }

    @Override
    public Long loginWithAccessToken(String token) {
    	String userIdStr = null;//cacheRepository.getUserIdByAccessToken(token);
    	if(userIdStr == null) {
    	    User user = userRepository.findByToken(token);
    	    if(user != null)
                return user.getUserId();
    	    else
    	        return 0L;
        }
    	return Long.valueOf(userIdStr);
    }
}
