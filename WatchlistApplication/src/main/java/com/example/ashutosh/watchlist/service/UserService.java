// src/main/java/com/example/ashutosh/watchlist/service/UserService.java
package com.example.ashutosh.watchlist.service;

import com.example.ashutosh.watchlist.dto.UserRegistrationDto;
import com.example.ashutosh.watchlist.Entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
@Service
public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
}