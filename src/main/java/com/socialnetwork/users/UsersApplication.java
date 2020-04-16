package com.socialnetwork.users;

import com.socialnetwork.users.domain.User;
import com.socialnetwork.users.repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.util.Arrays;

@SpringBootApplication
public class UsersApplication {

	private final static Logger log = LoggerFactory.getLogger(UsersApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(UserRepository userRepository) {
		return args -> {

			var users = userRepository.findAll();
			log.info("Lookup each person by name...");
			users.forEach(person -> log.info(
					"\t" + userRepository.findByName(person.getName()).toString()));
		};
	}
}
