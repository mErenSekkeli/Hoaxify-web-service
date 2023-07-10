package com.hoaxify.webservice;

import com.hoaxify.webservice.user.UserService;
import com.hoaxify.webservice.user.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(/*exclude = SecurityAutoConfiguration.class /*security pass geçiyor*/)
public class WebserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}

	/*
	@Bean
	public CommandLineRunner createInitialUsers(UserService userService) {
		return (args) -> {
			for(int i = 1; i <= 20; i++) {
				Users user = new Users();
				user.setName("name" + i);
				user.setSurname("surname" + i);
				user.setUserName("user" + i);
				user.setPass("p4ssword");
				userService.saveUser(user);
			}
		};
	}
	*/

}
