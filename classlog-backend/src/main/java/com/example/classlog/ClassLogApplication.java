package com.example.classlog;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
@EnableTransactionManagement
public class ClassLogApplication {

	@Value("${spring.datasource.url:NOT_SET}")
	private String datasourceUrl;

	@Value("${spring.profiles.active:NOT_SET}")
	private String activeProfile;
	public static void main(String[] args) {
		SpringApplication.run(ClassLogApplication.class, args);
	}
}
