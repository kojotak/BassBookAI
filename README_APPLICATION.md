# Bass Book AI - Spring Boot Application

## Description
A web application for storing play-along videos on YouTube for bass guitar. Built with Spring Boot and modern web technologies.

## Features
- Browse bass play-along videos in a searchable, filterable table
- Add new YouTube videos with metadata
- User authentication via Google/GitHub OAuth2
- Tag videos for personal organization (PLAY, PRACTICE, SLOWDOWN, TODO, FORGET)
- Filter by song name, artist, tuning, technique, and tags
- Expandable rows to view all YouTube channels for a song

## Tech Stack
- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Data JPA, OAuth2
- **Frontend**: HTML5, CSS3, JavaScript (ES6+), Bootstrap 5
- **Database**: H2 (development), PostgreSQL (production)
- **Build**: Maven

## Data Model
- **Channel**: YouTube channel with ID and display name
- **Artist**: Musical artist with associated songs
- **Song**: Individual song with artist reference
- **Tuning**: Bass guitar tunings (EADG, DADG, CGCF, BEADG)
- **Technique**: Playing techniques (slap, hammer on, etc.)
- **Tag**: User-specific states (PLAY, PRACTICE, SLOWDOWN, TODO, FORGET)
- **Video**: YouTube video with metadata and optional user tags

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Using Maven (Recommended)

1. **Clone and build:**
   ```bash
   git clone <repository-url>
   cd BassBookAI
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - Main page: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:bassbook`
     - Username: `sa`
     - Password: `password`

### Configuration

#### OAuth2 Setup (Optional)
To enable Google/GitHub login, set these environment variables:

**Google OAuth2:**
```
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

**GitHub OAuth2:**
```
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

#### YouTube API (Optional)
To enable real YouTube video information parsing:
```
youtube.api.key=your_youtube_api_key
```

Add this to `application.properties` or set as environment variable.

#### PostgreSQL Production Database
Uncomment the PostgreSQL configuration in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bassbook
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

## API Endpoints

### Public Endpoints
- `GET /api/videos/songs` - Get paginated songs with filters
- `GET /api/videos/songs/{id}/videos` - Get videos for a specific song
- `GET /api/artists` - Get all artists
- `GET /api/artists/{id}/songs` - Get songs for an artist
- `GET /api/enums/tunings` - Get available tunings
- `GET /api/enums/techniques` - Get available techniques
- `GET /api/enums/tags` - Get available tags
- `POST /api/youtube/parse` - Parse YouTube URL to extract video info

### Authenticated Endpoints
- `GET /api/user/info` - Get current user information
- `POST /api/videos` - Add a new video
- `PUT /api/videos/{id}/tag` - Update video tag
- `POST /api/artists` - Create new artist
- `POST /api/artists/{id}/songs` - Create new song for artist

## Development

### Project Structure
```
src/main/java/com/bassbook/
├── BassBookAiApplication.java     # Main application class
├── config/                        # Configuration classes
│   ├── AppConfig.java            # Bean configuration
│   └── SecurityConfig.java       # Security configuration
├── controller/                    # REST controllers
├── dto/                           # Data transfer objects
├── model/                         # JPA entities
├── repository/                    # Spring Data repositories
└── service/                       # Business logic services

src/main/resources/
├── static/                        # Static web assets
│   ├── css/
│   ├── js/
│   └── *.html                     # Frontend pages
└── application.properties         # Configuration
```

### Database Schema
The application uses JPA to automatically create the database schema:
- `channels` - YouTube channel information
- `artists` - Musical artists
- `songs` - Songs with artist references
- `videos` - YouTube videos with metadata
- `video_techniques` - Join table for video techniques

## Testing

### Run Tests
```bash
mvn test
```

### Integration Tests
The application includes integration tests for:
- Video management endpoints
- User authentication
- YouTube URL parsing
- Data filtering and pagination

## Production Deployment

### Docker Deployment
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/bass-book-ai-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Build and run:
```bash
docker build -t bass-book-ai .
docker run -p 8080:8080 bass-book-ai
```

### Environment Variables for Production
Set the following variables for production:
- Database connection details
- OAuth2 client credentials
- YouTube API key (optional)
- Server port

## License
This project is open source and available under the MIT License.