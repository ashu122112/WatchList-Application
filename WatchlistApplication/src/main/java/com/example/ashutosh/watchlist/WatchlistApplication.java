package com.example.ashutosh.watchlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WatchlistApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WatchlistApplication.class, args);
    }
}