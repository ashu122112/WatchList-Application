package com.example.ashutosh.watchlist.Entity;

import com.example.ashutosh.watchlist.Entity.valid.Priority;
import com.example.ashutosh.watchlist.Entity.valid.Rating;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne; // Import ManyToOne
import jakarta.persistence.JoinColumn; // Import JoinColumn
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message="Please enter the title")
	private String title;
	
	@Rating
	private float rating ;
	
	@Priority
	private String priority;
	
	@Size(max=50,message="Comment should be maximum 50 characters")
	private String comment;
	
    // Establish Many-to-One relationship with User
    // Each movie belongs to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column in 'movie' table
    private User user; // The user who owns this movie

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
