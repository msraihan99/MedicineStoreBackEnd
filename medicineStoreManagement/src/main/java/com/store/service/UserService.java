package com.store.service;

import com.store.model.User;

public interface UserService {

	User saveUser(User user);

	User login(String email, String rawPassword);

}
