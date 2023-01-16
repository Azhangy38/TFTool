package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


import java.io.IOException;

@SpringBootApplication
public class StartApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(StartApplication.class, args);
		System.out.println("Server is running");
	}

}

