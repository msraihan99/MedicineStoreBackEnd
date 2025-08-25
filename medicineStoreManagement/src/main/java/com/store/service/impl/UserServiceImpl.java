package com.store.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.controller.UserController;
import com.store.model.User;
import com.store.repository.UserRepository;
import com.store.service.PasswordService;
import com.store.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepo;

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User saveUser(User user) {
		if (user == null || user.getPassword() == null || user.getUsername() == null) {
			throw new IllegalArgumentException("Username and password must not be null");
		}

		try {
			String encryptedPassword = PasswordService.encrypt(user.getPassword());

			user.setPassword(encryptedPassword);
			user.setCreatedBy(user.getUsername());
			user.setCreatedOn(LocalDateTime.now());

			return userRepo.save(user);
		} catch (Exception e) {
			// Use logger instead of printStackTrace
			log.error("Error while saving user: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to save user", e);
		}

	}

	@Override
	public User login(String email, String rawPassword) {
		try {
			// Input validation
			if (email == null || rawPassword == null || email.isBlank() || rawPassword.isBlank()) {
				throw new IllegalArgumentException("Email or password must not be null or empty");
			}

			User user = userRepo.findByEmail(email);

			if (user == null) {
				throw new RuntimeException("No user found with the given email");
			}

			String encryptedInputPassword = PasswordService.encrypt(rawPassword);

			if (!encryptedInputPassword.equals(user.getPassword())) {
				throw new RuntimeException("Invalid credentials");
			}

			return user;

		} catch (Exception e) {
			System.err.println("Unexpected error during login: " + e.getMessage());
			 log.error("Unexpected error during login", e);
			return null;
		}

	}

}
