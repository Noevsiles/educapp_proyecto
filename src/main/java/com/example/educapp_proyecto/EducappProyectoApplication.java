package com.example.educapp_proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EducappProyectoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EducappProyectoApplication.class, args);
	}

}
