package com.example.coworking_prenotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoworkingPrenotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoworkingPrenotationApplication.class, args);
	}

}
