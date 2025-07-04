package com.example.ashutosh.watchlist.repository;

import java.util.List; // Import List
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ashutosh.watchlist.Entity.Movie;
import com.example.ashutosh.watchlist.Entity.User; // Import User

@Repository
public interface MovieRepo extends JpaRepository<Movie,Integer>{
    // New method to find all movies owned by a specific user
    List<Movie> findByUser(User user);
}
