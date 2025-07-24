package com.dndn.backend.dndn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DndnApplication {

	public static void main(String[] args) {
		SpringApplication.run(DndnApplication.class, args);
	}

}
