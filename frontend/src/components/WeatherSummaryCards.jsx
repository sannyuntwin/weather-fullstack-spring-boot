import React from 'react';
import './WeatherSummaryCards.css';

/**
 * WeatherSummaryCards component for displaying weather metrics in a grid.
 * Shows temperature, humidity, wind speed, and feels like temperature.
 */
const WeatherSummaryCards = ({ weather }) => {
  const summaryCards = [
    {
      title: 'Temperature',
      value: `${Math.round(weather.temperature)}°C`,
      icon: '🌡️',
      color: 'temperature',
      description: 'Current temperature'
    },
    {
      title: 'Feels Like',
      value: `${Math.round(weather.feelsLike)}°C`,
      icon: '🤲',
      color: 'feels-like',
      description: 'Perceived temperature'
    },
    {
      title: 'Humidity',
      value: `${weather.humidity}%`,
      icon: '💧',
      color: 'humidity',
      description: 'Moisture level'
    },
    {
      title: 'Wind Speed',
      value: `${weather.windSpeed} m/s`,
      icon: '💨',
      color: 'wind',
      description: 'Wind velocity'
    }
  ];

  return (
    <div className="weather-summary-cards">
      <h3 className="summary-title">📊 Weather Summary</h3>
      <div className="summary-grid">
        {summaryCards.map((card, index) => (
          <div key={index} className={`summary-card ${card.color}`}>
            <div className="card-icon">{card.icon}</div>
            <div className="card-content">
              <div className="card-title">{card.title}</div>
              <div className="card-value">{card.value}</div>
              <div className="card-description">{card.description}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default WeatherSummaryCards;
