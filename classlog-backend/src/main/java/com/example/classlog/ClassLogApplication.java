package com.example.classlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableTransactionManagement
public class ClassLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassLogApplication.class, args);
	}

}
