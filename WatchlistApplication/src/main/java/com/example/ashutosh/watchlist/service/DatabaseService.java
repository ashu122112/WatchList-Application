package com.example.ashutosh.watchlist.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ashutosh.watchlist.Entity.Movie;
import com.example.ashutosh.watchlist.repository.MovieRepo;
@Service
public class DatabaseService {
	@Autowired
	MovieRepo movieRepo;
	@Autowired
	RatingService ratingService;
	
	public void create(Movie movie) {
		
		String rating=ratingService.getMovieRating(movie.getTitle());
		if(rating!=null) {
			movie.setRating(Float.parseFloat(rating));
		}
		
		movieRepo.save(movie);
	}
	
	public List<Movie> getAllMovies(){
		return movieRepo.findAll();
		
	}
	
	public Movie getMovieById(Integer id) {
		// TODO Auto-generated method stub
		return movieRepo.findById(id)
		        .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + id));
		
	}

	public void update(Movie movie, Integer id) { 	 	
		// TODO Auto-generated method stub
		Movie toBeUpdated=getMovieById(id);
		toBeUpdated.setTitle(movie.getTitle());
		toBeUpdated.setRating(movie.getRating());
		toBeUpdated.setPriority(movie.getPriority());
		toBeUpdated.setComment(movie.getComment());
		
		movieRepo.save(toBeUpdated);
	}

	public void delete(Integer id) {
        movieRepo.deleteById(id);
    }

	
}
