package com.example.ashutosh.watchlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import Model
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest; // Import HttpServletRequest

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) { // Add HttpServletRequest and Model
        System.out.println("LoginController: Displaying login page.");
        model.addAttribute("currentPath", request.getRequestURI()); // Add currentPath to the model
        return "login"; // Make sure you have login.html in templates
    }
}
