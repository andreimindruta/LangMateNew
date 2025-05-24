package com.example.langmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
@EntityScan
public class LangmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(LangmateApplication.class, args);
	}

}
