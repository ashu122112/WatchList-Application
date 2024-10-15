package com.example.ashutosh.watchlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan("com.example.ashutosh.watchlist")
@EnableJpaRepositories(basePackages = "com.example.ashutosh.watchlist.repository")

public class WatchlistApplication {

	public static void main(String[] args) {
		SpringApplication.run(WatchlistApplication.class, args);
	}

}
