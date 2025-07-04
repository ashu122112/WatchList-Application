// src/main/java/com/example/ashutosh/watchlist/controller/UserRegistrationController.java
package com.example.ashutosh.watchlist.controller;

import com.example.ashutosh.watchlist.dto.UserRegistrationDto;
import com.example.ashutosh.watchlist.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/save")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.save(registrationDto);
        // Change: Redirect to the login page after successful registration
        return "redirect:/login?registrationSuccess"; // Added a parameter for potential success message on login page
    }
}
