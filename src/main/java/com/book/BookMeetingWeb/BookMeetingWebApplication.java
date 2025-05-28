package com.book.BookMeetingWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BookMeetingWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookMeetingWebApplication.class, args);

	}
}
