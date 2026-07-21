package com.amine.urticare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrticareApplication {
	public static void main(String[] args) {
		SpringApplication.run(UrticareApplication.class, args);
	}
}
