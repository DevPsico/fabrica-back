package com.estacionamento.estacionamento.profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestProfile {
	
	@Autowired
	private DBService dbService;
	
	
	@Bean
	public DBService instanciaDB() {
		
		this.dbService.instanciaDB();
		return dbService;
	}

}
