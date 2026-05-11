import React from 'react';
import './WeatherCard.css';

/**
 * WeatherCard component for displaying weather information.
 * This component receives weather data as props and displays it in a clean card format.
 */
const WeatherCard = ({ weather }) => {
  // Function to get the weather icon URL from OpenWeather
  const getWeatherIconUrl = (iconCode) => {
    return `https://openweathermap.org/img/wn/${iconCode}@2x.png`;
  };

  // Function to capitalize the first letter of weather description
  const capitalizeFirstLetter = (string) => {
    return string.charAt(0).toUpperCase() + string.slice(1);
  };

  return (
    <div className="weather-card">
      <div className="weather-header">
        <div className="location-info">
          <h2 className="city-name">
            {weather.city}, {weather.country}
          </h2>
          <p className="weather-description">
            {capitalizeFirstLetter(weather.description)}
          </p>
        </div>
        <div className="weather-icon">
          <img
            src={getWeatherIconUrl(weather.icon)}
            alt={weather.description}
            className="icon"
          />
        </div>
      </div>

      <div className="weather-main">
        <div className="temperature-info">
          <div className="main-temp">
            <span className="temp-value">{Math.round(weather.temperature)}</span>
            <span className="temp-unit">°C</span>
          </div>
          <div className="feels-like">
            Feels like {Math.round(weather.feelsLike)}°C
          </div>
        </div>
      </div>

      <div className="weather-details">
        <div className="detail-item">
          <div className="detail-label">Humidity</div>
          <div className="detail-value">{weather.humidity}%</div>
        </div>
        <div className="detail-item">
          <div className="detail-label">Wind Speed</div>
          <div className="detail-value">{weather.windSpeed} m/s</div>
        </div>
      </div>
    </div>
  );
};

export default WeatherCard;
