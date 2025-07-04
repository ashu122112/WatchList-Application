 Watchlist Application
A Spring Boot-based web application designed to help movie enthusiasts organize, track, and manage their personal movie watchlists. Users can register, log in, add new movies, view movie details (including fetched IMDb ratings), and update or delete entries from their watchlist.

✨ Features
User Authentication & Authorization: Secure registration and login functionality using Spring Security.

Personalized Watchlists: Each registered user has their own private watchlist.

Add Movies: Easily add new movies to your watchlist.

Automatic IMDb Rating Fetching: When adding a movie, the application attempts to fetch its IMDb rating automatically using the OMDB API. Users can also manually enter a rating.

Update Movie Details: Edit existing movie entries, including title, priority, comment, and rating.

Delete Movies: Remove movies from your watchlist.

Responsive UI: A clean and responsive user interface built with Thymeleaf, Bootstrap, and custom CSS, ensuring a good experience on various devices.

Database Persistence: Movie and user data is persistently stored in a MySQL database.

🛠️ Technologies Used
Backend:

Spring Boot (3.x)

Spring Security

Spring Data JPA (Hibernate)

MySQL Database

REST Template (for OMDB API calls)

Frontend:

Thymeleaf (Templating Engine)

Bootstrap 4.x (CSS Framework)

Font Awesome (Icons)

Custom CSS

JavaScript (for dynamic elements like delete confirmation modal)

External API:

OMDB API (for movie ratings)

🚀 Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
Before you begin, ensure you have the following installed:

Java Development Kit (JDK): Version 17 or higher.

Maven: For building and managing project dependencies.

MySQL Server: A running MySQL database instance.

OMDB API Key: Obtain a free API key from http://www.omdbapi.com/apikey.aspx.

Database Setup
Create Database: Create a new database named watchmovie in your MySQL server.

CREATE DATABASE watchmovie;



Configure application.properties:
Open src/main/resources/application.properties and update the database connection details and your OMDB API key:

# Database Connection
spring.datasource.url=jdbc:mysql://localhost:3306/watchmovie
spring.datasource.username=root # Your MySQL username
spring.datasource.password=ashu122112 # Your MySQL password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update # Use 'update' for development, 'none' for production

# OMDB API Key (replace with your actual key)
# Ensure this is correctly set in your RatingService.java as well if hardcoded there
# (e.g., in RatingService.java: String apiUrl="https://www.omdbapi.com/?apikey=YOUR_OMDB_API_KEY&t=";)



Note: For spring.jpa.hibernate.ddl-auto, update is suitable for development as it will create/update tables automatically without dropping data. For production, consider none and manage schema migrations manually.

Initial Role Data (Optional but Recommended):
Spring Security expects roles to exist. You might need to manually insert the ROLE_USER role into your roles table if your application doesn't automatically populate it on first run (which it should if ddl-auto is create or create-drop initially).

INSERT INTO roles (name) VALUES ('ROLE_USER');



Running the Application
Clone the repository:

git clone https://github.com/your-username/watchlist-app.git # Replace with your actual repo URL
cd watchlist-app



Build the project:

mvn clean install



Run the application:

mvn spring-boot:run



The application will start on http://localhost:8080.

Troubleshooting
"Add a README" still showing on GitHub: If you've pushed the README.md file but GitHub still prompts you to add one, ensure the README.md file is located directly in the root directory of your repository, not inside a subfolder. Sometimes, a browser cache refresh (Ctrl + F5 or Cmd + Shift + R) might also be needed.

🖥️ Usage
Access the Application: Open your web browser and navigate to http://localhost:8080.

Register: Click on the "Register" link to create a new user account. After successful registration, you will be redirected to the login page.

Login: Use your newly created credentials to log in.

Manage Watchlist:

Add New Movie: Click "Add Item" in the navigation bar to open the movie submission form. Enter the movie title, priority (L/M/H), and comments. The IMDb rating will be fetched automatically if available, or you can enter it manually.

View Watchlist: Go to the "Watchlist" page to see all your added movies.

Update Movie: Click "Update" next to a movie entry to modify its details.

Delete Movie: Click "Delete" next to a movie entry. A confirmation modal will appear before deletion.