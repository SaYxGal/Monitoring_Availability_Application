package com.test.subapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SubapplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubapplicationApplication.class, args);
	}
	@GetMapping("/get")
	public ResponseEntity<String> testGetRequest(){
		return new ResponseEntity<>("Response", HttpStatus.OK);
	}

}
