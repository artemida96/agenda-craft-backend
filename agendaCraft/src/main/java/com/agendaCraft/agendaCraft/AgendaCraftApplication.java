package com.agendaCraft.agendaCraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.agendaCraft.agendaCraft", "com.agendaCraft.agendaCraft.service"})


public class AgendaCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaCraftApplication.class, args);
	}

}
