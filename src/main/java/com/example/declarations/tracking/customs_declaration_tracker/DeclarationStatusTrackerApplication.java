package com.example.declarations.tracking.customs_declaration_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DeclarationStatusTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeclarationStatusTrackerApplication.class, args);
	}

}
