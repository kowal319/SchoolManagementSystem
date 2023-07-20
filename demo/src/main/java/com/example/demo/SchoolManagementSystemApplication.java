package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
			title = "School Management System",
			version = "1.0.0",
			description = "School Management System for recruitment process. Applications using CRUD operations in Java and SpringBoot"
	)
			)
public class SchoolManagementSystemApplication {
	public static void main(String[] args) {

		SpringApplication.run(SchoolManagementSystemApplication.class, args);
	}

	}



