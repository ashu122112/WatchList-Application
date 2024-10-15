package com.example.ashutosh.watchlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ashutosh.watchlist.Entity.Movie;

@Repository
public interface MovieRepo extends JpaRepository<Movie,Integer>{

}
