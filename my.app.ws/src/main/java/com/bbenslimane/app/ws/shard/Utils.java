package com.bbenslimane.app.ws.shard;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;


//to be injectable anywhere
@Component 
public class Utils {
	
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuwxyz";
	
	public String generateStringId(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		
		for(int i=0; i< length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return new String(returnValue);
		
	}

}