package com.example.ashutosh.watchlist.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException; // Import for HTTP client errors
import org.springframework.web.client.ResourceAccessException; // Import for network errors like connection timed out

import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class RatingService {
	// IMPORTANT: Your OMDB API key has been added here.
	// You can get one from http://www.omdbapi.com/apikey.aspx
	String apiUrl="https://www.omdbapi.com/?apikey=9f5c813e&t="; // Your provided API key
	
	public String getMovieRating(String title) {
		System.out.println("RatingService: Attempting to fetch rating for movie: " + title);
		try {
			RestTemplate template=new RestTemplate();
			ResponseEntity<ObjectNode> response=template.getForEntity(apiUrl + title, ObjectNode.class);
			
			ObjectNode jsonObject=response.getBody();
            
            if (jsonObject != null && jsonObject.has("imdbRating") && !jsonObject.path("imdbRating").asText().equals("N/A")) {
                String imdbRating = jsonObject.path("imdbRating").asText();
                System.out.println("RatingService: Successfully fetched IMDb Rating for '" + title + "': " + imdbRating);
                return imdbRating;
            } else {
                System.out.println("RatingService: IMDb Rating not found or 'N/A' for movie: " + title + ". OMDB Response: " + (jsonObject != null ? jsonObject.toString() : "null"));
                return null; // Return null if rating is not available or "N/A"
            }
			
		} catch (ResourceAccessException e) {
            // This catches network errors like "Connection timed out"
            System.err.println("RatingService: Network/Connection error when fetching rating for '" + title + "': " + e.getMessage());
            System.err.println("  Possible causes: No internet, firewall blocking, incorrect API URL, or OMDB API is down/unreachable.");
            e.printStackTrace(); // Print stack trace for detailed debugging
            return null;
        }
        catch (HttpClientErrorException e) {
            // This catches HTTP client errors (4xx status codes)
            System.err.println("RatingService: HTTP Client Error when fetching rating for '" + title + "': Status " + e.getStatusCode() + " - Response Body: " + e.getResponseBodyAsString());
            System.err.println("  Possible causes: Invalid API key, rate limit exceeded, or movie not found by OMDB.");
            e.printStackTrace(); // Print stack trace for detailed debugging
            return null;
        } catch(Exception e) {
			// Catch any other unexpected exceptions
			System.err.println("RatingService: General error when fetching rating for '" + title + "': " + e.getMessage());
			e.printStackTrace(); // Print stack trace for detailed debugging
			return null;
		}
	}
	 
}
