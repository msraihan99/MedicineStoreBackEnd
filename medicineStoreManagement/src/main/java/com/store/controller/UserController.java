package com.store.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.LoginRequest;
import com.store.model.User;
import com.store.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
	    log.info("Inside registerUser() :: UserController");

	    try {
	        User savedUser = userService.saveUser(user);
	        if (savedUser == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
	                "status", "error",
	                "message", "User registration failed"
	            ));
	        }

	        return ResponseEntity.ok(Map.of(
	            "status", "success",
	            "message", "User registered successfully",
	            "user", savedUser
	        ));

	    } catch (Exception e) {
	        log.error("Exception in registerUser(): ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
	            "status", "error",
	            "message", "An unexpected error occurred"
	        ));
	    }
	}
	
	
	
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest login) {
		log.info("Inside loginUser() :: UserController , Email - "+ login.getEmail());
	    try {
	        User user = userService.login(login.getEmail(), login.getPassword());

	        if (user != null) {
	            return ResponseEntity.ok(Map.of(
	                "status", "success",
	                "message", "Login successful",
	                "user", user
	            ));
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
	                "status", "error",
	                "message", "Invalid email or password"
	            ));
	        }

	    } catch (Exception e) {
	        // For unexpected errors
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
	            "status", "error",
	            "message", "An unexpected error occurred. Please try again."
	        ));
	    }
	}


	
	
	
}
