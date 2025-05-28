package com.teamproject.TP_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TpBackendApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TpBackendApplication.class);
		application.setWebApplicationType(WebApplicationType.SERVLET);
		application.run(args);
	}
}