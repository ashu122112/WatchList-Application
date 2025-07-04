// src/main/java/com/example/ashutosh/watchlist/repository/UserRepository.java
package com.example.ashutosh.watchlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ashutosh.watchlist.Entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}