// src/main/java/com/example/ashutosh/watchlist/repository/RoleRepository.java
package com.example.ashutosh.watchlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ashutosh.watchlist.Entity.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}