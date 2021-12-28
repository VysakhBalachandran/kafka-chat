package com.endpoint.kafkamessaging.auth.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
	
	private String accessToken;
	private Long sendTo;
	private String msg;
	
	
}

