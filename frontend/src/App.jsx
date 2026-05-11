import React, { useState } from 'react';
import WeatherCard from './components/WeatherCard';
import './App.css';

/**
 * Main App component for the Weather Application.
 * This component manages the state for city search and weather data display.
 */
function App() {
  const [city, setCity] = useState('');
  const [weatherData, setWeatherData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  /**
   * Handles the weather search functionality.
   * Calls the backend API to fetch weather data for the specified city.
   */
  const handleSearch = async () => {
    if (!city.trim()) {
      setError('Please enter a city name');
      return;
    }

    setLoading(true);
    setError('');
    setWeatherData(null);

    try {
      // Call the backend API endpoint
      const response = await fetch(`http://localhost:8080/api/weather/${city}`);
      
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to fetch weather data');
      }

      const data = await response.json();
      setWeatherData(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handles form submission (Enter key press)
   */
  const handleSubmit = (e) => {
    e.preventDefault();
    handleSearch();
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Weather App</h1>
        <p>Get current weather information for any city</p>
      </header>

      <main className="app-main">
        {/* Search Form */}
        <form className="search-form" onSubmit={handleSubmit}>
          <div className="search-input-group">
            <input
              type="text"
              value={city}
              onChange={(e) => setCity(e.target.value)}
              placeholder="Enter city name..."
              className="search-input"
              disabled={loading}
            />
            <button
              type="submit"
              className="search-button"
              disabled={loading}
            >
              {loading ? 'Searching...' : 'Search'}
            </button>
          </div>
        </form>

        {/* Error Message */}
        {error && (
          <div className="error-message">
            <p>{error}</p>
          </div>
        )}

        {/* Weather Card */}
        {weatherData && <WeatherCard weather={weatherData} />}

        {/* Loading State */}
        {loading && (
          <div className="loading">
            <p>Loading weather data...</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
