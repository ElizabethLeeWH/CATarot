package com.catarot.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.catarot.server.service.TarotService;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner{

	@Autowired
	TarotService tarotService;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
	}

}
