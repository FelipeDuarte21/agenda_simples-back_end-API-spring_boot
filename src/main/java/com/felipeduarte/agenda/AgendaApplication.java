package com.felipeduarte.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class AgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaApplication.class, args);
	}

}
