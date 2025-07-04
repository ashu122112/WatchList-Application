package com.example.ashutosh.watchlist.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException; 
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.ashutosh.watchlist.Entity.Movie;
import com.example.ashutosh.watchlist.Entity.User; 
import com.example.ashutosh.watchlist.service.DatabaseService;
import com.example.ashutosh.watchlist.repository.UserRepository; 

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Controller
public class MovieController {
    @Autowired
    DatabaseService databaseService;
    @Autowired 
    UserRepository userRepository; 
    
    // Helper method to get the current authenticated User entity
    private User getCurrentUser(UserDetails userDetails) {
        if (userDetails == null) {
            System.out.println("MovieController: No authenticated user details found. Throwing UsernameNotFoundException.");
            throw new UsernameNotFoundException("User not authenticated.");
        }
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            System.out.println("MovieController: User entity not found in DB for username: " + userDetails.getUsername() + ". This indicates a data inconsistency.");
            throw new UsernameNotFoundException("User not found in database.");
        }
        return user;
    }

    @GetMapping("/index")
    public String showHomePage(HttpServletRequest request, Map<String, Object> model) {
        System.out.println("MovieController: Displaying home page.");
        model.put("currentPath", request.getRequestURI());
        return "index";  
    }
    
    @GetMapping("/watchlistItemForm")
    public ModelAndView showWatchListForm(@RequestParam(required=false) Integer id, 
                                          HttpServletRequest request,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        String viewName = "watchlistItemForm";
        Map<String, Object> model = new HashMap<>();
        model.put("currentPath", request.getRequestURI());

        User currentUser = getCurrentUser(userDetails); // Get current authenticated user

        if(id == null) {
            System.out.println("MovieController: Displaying form for new movie item for user: " + currentUser.getUsername());
            model.put("watchlistItem", new Movie());
        } else {
            try {
                Optional<Movie> movieOptional = databaseService.getMovieById(id, currentUser); 
                if (movieOptional.isPresent()) {
                    Movie movie = movieOptional.get();
                    System.out.println("MovieController: Displaying form for updating movie item with ID: " + id + " for user: " + currentUser.getUsername());
                    model.put("watchlistItem", movie);
                } else {
                    System.out.println("MovieController: Movie with ID " + id + " not found or not owned by user " + currentUser.getUsername() + ". Displaying form for new movie.");
                    // If movie not found or not owned, treat as adding a new movie
                    model.put("watchlistItem", new Movie()); 
                }
            } catch (Exception e) { 
                System.err.println("MovieController: Error retrieving movie ID " + id + ": " + e.getMessage());
                e.printStackTrace();
                model.put("watchlistItem", new Movie()); // Fallback to new movie on error
            }
        }
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/watchlistItem") // Corrected mapping to match form action
    public ModelAndView submitWatchlistItem(@Valid @ModelAttribute("watchlistItem") Movie movie, 
                                          BindingResult bindingResult,
                                          @AuthenticationPrincipal UserDetails userDetails,
                                          HttpServletRequest request) { 
        
        User currentUser = getCurrentUser(userDetails); 

        if(bindingResult.hasErrors()) {
            System.out.println("MovieController: Validation errors found when submitting movie item for user: " + currentUser.getUsername());
            ModelAndView modelAndView = new ModelAndView("watchlistItemForm");
            modelAndView.addObject("currentPath", request.getRequestURI()); 
            return modelAndView;
        }
        
        Integer id = movie.getId();
        try {
            if(id == null) {
                System.out.println("MovieController: Creating new movie: " + movie.getTitle() + " for user: " + currentUser.getUsername());
                databaseService.create(movie, currentUser); 
            } else {
                System.out.println("MovieController: Updating movie ID " + id + ": " + movie.getTitle() + " for user: " + currentUser.getUsername());
                databaseService.update(movie, id, currentUser); 
            }
        } catch (SecurityException e) {
            System.err.println("MovieController: Authorization error for user " + currentUser.getUsername() + ": " + e.getMessage());
            RedirectView rd = new RedirectView();
            rd.setUrl("/watchlist?error=unauthorized"); 
            return new ModelAndView(rd);
        } catch (NoSuchElementException e) { 
            System.err.println("MovieController: Movie not found error for user " + currentUser.getUsername() + " and movie ID " + id + ": " + e.getMessage());
            RedirectView rd = new RedirectView();
            rd.setUrl("/watchlist?error=notFound"); 
            return new ModelAndView(rd);
        }
        catch (Exception e) {
            System.err.println("MovieController: General error saving/updating movie for user " + currentUser.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
            RedirectView rd = new RedirectView();
            rd.setUrl("/watchlist?error=saveFailed"); 
            return new ModelAndView(rd);
        }
        
        RedirectView rd = new RedirectView();
        rd.setUrl("/watchlist");
        System.out.println("MovieController: Movie item saved/updated successfully. Redirecting to /watchlist.");
        return new ModelAndView(rd);
    }
    
    @GetMapping("/watchlist")
    public ModelAndView getWatchlist(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        String viewName = "watchlist";
        Map<String, Object> model = new HashMap<>();
        model.put("currentPath", request.getRequestURI());

        User currentUser = getCurrentUser(userDetails); 

        List<Movie> movieList = databaseService.getAllMovies(currentUser); 
        model.put("watchlistrows", movieList);
        model.put("noofmovies", movieList.size());
        model.put("username", userDetails != null ? userDetails.getUsername() : "");
        System.out.println("MovieController: Displaying watchlist for user: " + currentUser.getUsername());
        System.out.println("  Number of movies: " + movieList.size());
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/deleteWatchlistItem")
    public RedirectView deleteMovie(@RequestParam Integer id,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails); 
        try {
            System.out.println("MovieController: Deleting movie with ID: " + id + " for user: " + currentUser.getUsername());
            databaseService.delete(id, currentUser);  
            System.out.println("MovieController: Movie deleted. Redirecting to /watchlist.");
        } catch (SecurityException e) {
            System.err.println("MovieController: Authorization error during delete for user " + currentUser.getUsername() + ": " + e.getMessage());
            return new RedirectView("/watchlist?error=unauthorizedDelete");
        } catch (NoSuchElementException e) { 
            System.err.println("MovieController: Movie not found error for user " + currentUser.getUsername() + " and movie ID " + id + ": " + e.getMessage());
            return new RedirectView("/watchlist?error=notFound");
        }
        catch (Exception e) {
            System.err.println("MovieController: General error deleting movie ID " + id + " for user " + currentUser.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
            return new RedirectView("/watchlist?error=deleteFailed");
        }
        return new RedirectView("/watchlist");
    }
    
    @GetMapping("/")
    public String redirectToHome() {
        System.out.println("MovieController: Redirecting root URL to /index.");
        return "redirect:/index";
    }
}
