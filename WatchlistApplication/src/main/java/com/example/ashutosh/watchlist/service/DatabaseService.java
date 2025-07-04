package com.example.ashutosh.watchlist.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional; // Import Optional

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import com.example.ashutosh.watchlist.Entity.Movie;
import com.example.ashutosh.watchlist.Entity.User; // Import User entity
import com.example.ashutosh.watchlist.repository.MovieRepo;

@Service
public class DatabaseService {
	@Autowired
	MovieRepo movieRepo;
	@Autowired
	RatingService ratingService;
	
	@Transactional
	public void create(Movie movie, User user) { // Added User parameter
		System.out.println("DatabaseService: Creating new movie for user: " + user.getUsername());
		String rating=ratingService.getMovieRating(movie.getTitle());
		if(rating!=null) {
			movie.setRating(Float.parseFloat(rating));
		} else {
            movie.setRating(0.0f); // Default to 0.0 if rating not found
        }
		
        movie.setUser(user); // Associate the movie with the current user
		movieRepo.save(movie);
        System.out.println("DatabaseService: Movie '" + movie.getTitle() + "' saved for user " + user.getUsername());
	}
	
	public List<Movie> getAllMovies(User user){ // Added User parameter
		System.out.println("DatabaseService: Fetching all movies for user: " + user.getUsername());
		// Fetch movies specifically for the authenticated user
		return movieRepo.findByUser(user); 
	}
	
	public Optional<Movie> getMovieById(Integer id, User user) { // Added User parameter, returns Optional
		System.out.println("DatabaseService: Attempting to retrieve movie ID: " + id + " for user: " + user.getUsername());
		// Find movie by ID and ensure it belongs to the current user
		Optional<Movie> movieOptional = movieRepo.findById(id);
		if (movieOptional.isPresent() && movieOptional.get().getUser().getId().equals(user.getId())) {
			System.out.println("DatabaseService: Movie ID " + id + " found and owned by user " + user.getUsername());
			return movieOptional;
		} else {
			System.out.println("DatabaseService: Movie ID " + id + " not found or not owned by user " + user.getUsername());
			return Optional.empty(); // Return empty if not found or not owned
		}
	}

	@Transactional
	public void update(Movie movie, Integer id, User user) { // Added User parameter
		System.out.println("DatabaseService: Attempting to update movie ID: " + id + " for user: " + user.getUsername());
		Optional<Movie> toBeUpdatedOptional = getMovieById(id, user); // Use getMovieById with user check
		
		if (toBeUpdatedOptional.isEmpty()) {
			System.err.println("DatabaseService: Update failed. Movie ID " + id + " not found or not owned by user " + user.getUsername());
			throw new NoSuchElementException("Movie not found or unauthorized for update with id: " + id);
		}
		
		Movie toBeUpdated = toBeUpdatedOptional.get();
		toBeUpdated.setTitle(movie.getTitle());
		toBeUpdated.setRating(movie.getRating());
		toBeUpdated.setPriority(movie.getPriority());
		toBeUpdated.setComment(movie.getComment());
		// No need to set user again, it's already set and verified by getMovieById

		movieRepo.save(toBeUpdated);
        System.out.println("DatabaseService: Movie ID " + id + " updated successfully by user " + user.getUsername());
	}

	@Transactional
	public void delete(Integer id, User user) { // Added User parameter
		System.out.println("DatabaseService: Attempting to delete movie ID: " + id + " for user: " + user.getUsername());
		Optional<Movie> movieOptional = getMovieById(id, user); // Use getMovieById with user check
		
		if (movieOptional.isEmpty()) {
			System.err.println("DatabaseService: Delete failed. Movie ID " + id + " not found or not owned by user " + user.getUsername());
			throw new NoSuchElementException("Movie not found or unauthorized for delete with id: " + id);
		}
		
		movieRepo.delete(movieOptional.get());
        System.out.println("DatabaseService: Movie ID " + id + " deleted successfully by user " + user.getUsername());
	}
}
