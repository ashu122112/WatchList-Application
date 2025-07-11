// src/main/java/com/example/ashutosh/watchlist/dto/UserRegistrationDto.java
package com.example.ashutosh.watchlist.dto;

public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
    
    public UserRegistrationDto() {
    }
    
    public UserRegistrationDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}