package com.buyani.buyaniserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BuyAniServerWebApplication {

	@Value("${twilio.accountSId}")
	private static String accountSId;

	@Value("${twilio.authId}")
	private static String authId;

	public static void main(String[] args) {
		SpringApplication.run(BuyAniServerWebApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(); 
	}

}
