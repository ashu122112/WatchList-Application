package com.example.ashutosh.watchlist.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class RatingService {
	String apiUrl="https://www.omdbapi.com/?apikey=9e0ce7d&t=";
	
	public String getMovieRating(String title) {
		try {
			//try to fetch the rating by calling omdb api
			RestTemplate template=new RestTemplate();
			//apiUrl+title
			ResponseEntity<ObjectNode> response=template.getForEntity(apiUrl+title,ObjectNode.class);
			
			ObjectNode jsonObject=response.getBody();
			System.out.println(jsonObject.path("imdbRating").asText());
			return jsonObject.path("imdbRating").asText();
			
		}catch(Exception e) {
			System.out.println("Either api is down or movie name not available"+e.getMessage());
			return null;
			
		}
	}
	 
}







