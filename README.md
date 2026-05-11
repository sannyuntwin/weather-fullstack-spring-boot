# 🌾 Smart Agriculture Weather App

A comprehensive full-stack weather application with React frontend and Spring Boot backend that integrates with the OpenWeather API to provide **agriculture-specific weather advice** for farmers and gardeners.

## Architecture

```
React Frontend (Vite) → Spring Boot Backend → OpenWeather API
```

## Project Structure

```
weather-fullstack/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/weatherapi/
│   │   │   │   ├── WeatherApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── WeatherController.java
│   │   │   │   ├── service/
│   │   │   │   │   └── WeatherService.java
│   │   │   │   └── model/
│   │   │   │   │       ├── WeatherResponse.java
│   │   │   │   │       ├── AgricultureAdvice.java
│   │   │   │   │       └── OpenWeatherResponse.java
│   │   │   │   └── resources/
│   │   │   │       └── application.properties
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── WeatherCard.jsx
│   │   │   └── WeatherCard.css
│   │   ├── App.jsx
│   │   ├── App.css
│   │   ├── main.jsx
│   │   └── index.css
│   ├── package.json
│   ├── vite.config.js
│   ├── nginx.conf
│   └── Dockerfile
│
├── docker-compose.yml
└── README.md
```

## Setup Instructions

### 1. Get OpenWeather API Key

1. Visit [OpenWeather API](https://openweathermap.org/api)
2. Sign up for a free account
3. Get your API key from your account dashboard

### 2. Configure Backend API Key

Edit `backend/src/main/resources/application.properties`:

```properties
# Replace YOUR_API_KEY_HERE with your actual OpenWeather API key
openweather.api.key=your_actual_api_key_here
```

### 3. Running the Application

#### Option 1: Using Docker Compose (Recommended)

1. **Create environment file (optional):**
   ```bash
   # Create .env file in the project root
   echo "OPENWEATHER_API_KEY=your_actual_api_key_here" > .env
   ```

2. **Build and run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

3. **Access the application:**
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8080

4. **Stop the application:**
   ```bash
   docker-compose down
   ```

#### Option 2: Run Locally (Development)

**Backend Setup:**

1. Navigate to backend directory:
   ```bash
   cd backend
   ```

2. Configure API key in `src/main/resources/application.properties`

3. Run the Spring Boot application:
   ```bash
   # Using Maven
   mvn spring-boot:run
   
   # Or build and run JAR
   mvn clean package
   java -jar target/weather-api-0.0.1-SNAPSHOT.jar
   ```

**Frontend Setup:**

1. Navigate to frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Access the application at http://localhost:5173

## API Endpoints

### Backend API

- **GET** `/api/weather/{city}`
  - Returns weather data for the specified city
  - Example: `GET /api/weather/NewYork`

**Response Format:**
```json
{
  "city": "New York",
  "country": "US",
  "temperature": 22.5,
  "feelsLike": 21.8,
  "humidity": 65,
  "windSpeed": 3.2,
  "description": "partly cloudy",
  "icon": "02d",
  "agricultureAdvice": {
    "irrigationAdvice": "💧 High humidity - Reduce irrigation to prevent waterlogging",
    "temperatureAdvice": "🌤️ Optimal temperature - Normal watering schedule",
    "humidityAdvice": "🦠 High humidity - Check crops for disease risk, ensure proper air circulation",
    "generalAdvice": "🍃 Moderate winds - Good for pollination, monitor for wind damage."
  }
}
```

**Error Response:**
```json
{
  "message": "City not found: InvalidCity"
}
```

## 🌟 Features

### 🌱 Agriculture Intelligence
- ✅ **Smart Irrigation Advice** - Recommends irrigation based on rain forecasts and humidity levels
- ✅ **Temperature-Based Guidance** - Provides watering schedules for extreme temperatures
- ✅ **Disease Risk Warnings** - Alerts farmers when humidity conditions favor crop diseases
- ✅ **General Farming Tips** - Wind conditions, field work recommendations, and seasonal advice

### Frontend (React)
- ✅ Modern React with Vite
- ✅ Responsive design with CSS Grid and Flexbox
- ✅ Weather search functionality
- ✅ Beautiful weather cards with icons
- ✅ **Agriculture advice display** with color-coded categories
- ✅ Error handling and loading states
- ✅ Clean, beginner-friendly code with comments

### Backend (Spring Boot)
- ✅ Java 17 with Spring Boot 3.2.5
- ✅ RESTful API with proper error handling
- ✅ OpenWeather API integration
- ✅ **Agriculture advice engine** with intelligent weather analysis
- ✅ CORS configuration for frontend
- ✅ Clean architecture with Service and Controller layers
- ✅ Detailed comments for beginners

### Docker & Deployment
- ✅ Multi-stage Docker builds for optimization
- ✅ Docker Compose for easy development and deployment
- ✅ Nginx for serving React production build
- ✅ Health checks and proper networking

## Testing the Application

1. **Test Backend API directly:**
   ```bash
   curl http://localhost:8080/api/weather/London
   ```

2. **Test Frontend:**
   - Open http://localhost:5173 in your browser
   - Enter a city name (e.g., "London", "New York", "Tokyo")
   - Click "Search" to see weather data

## Troubleshooting

### Common Issues

1. **"City not found" error:**
   - Check if the city name is spelled correctly
   - Try using city, country format (e.g., "London,GB")

2. **"API key not configured" error:**
   - Ensure you've set your OpenWeather API key in `application.properties`
   - Verify the API key is valid and active

3. **CORS errors:**
   - Make sure the backend is running on port 8080
   - Check that CORS is properly configured in the backend

4. **Docker build issues:**
   - Ensure Docker is running and you have sufficient disk space
   - Try running `docker system prune` to clean up old containers

### Development Tips

- **Backend logs:** Check Spring Boot console output for detailed error messages
- **Frontend debugging:** Use browser developer tools to inspect network requests
- **API testing:** Use Postman or curl to test the backend API directly

## Technologies Used

### Backend
- Java 17
- Spring Boot 3.2.5
- Spring Web (REST API)
- Spring WebFlux (WebClient for HTTP calls)
- Maven (Build tool)
- Jackson (JSON processing)

### Frontend
- React 18
- Vite (Build tool)
- CSS3 (Styling)
- OpenWeather Icons

### DevOps
- Docker
- Docker Compose
- Nginx (Web server)

## License

This project is for educational purposes. Feel free to use and modify as needed.
