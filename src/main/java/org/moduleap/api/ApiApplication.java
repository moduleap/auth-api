package org.moduleap.api;

import org.moduleap.api.controllers.ItemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients
@RestController
public class ApiApplication {

	@Autowired
	private ItemClient itemClient;
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@PostMapping("/item")
	public ResponseEntity<?> createItem(){
		return new ResponseEntity(itemClient.createItem(), HttpStatus.OK);
	}

}
