import React, { useState, useEffect } from 'react';
import FullMap from './components/MapCard';
import Sidebar from './components/Sidebar';
import BottomPanel from './components/BottomPanel';
import './App.css';

/**
 * Strategic Agriculture Command Center App
 */
function App() {
  const [city, setCity] = useState('');
  const [weatherData, setWeatherData] = useState(null);
  const [activeField, setActiveField] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Initial load
  useEffect(() => {
    getCurrentLocation();
    fetchFields();
  }, []);

  const getCurrentLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          handleSearchByCoords(latitude, longitude);
        },
        (error) => {
          console.error("Geolocation error:", error);
          handleSearch('Campo Grande'); // Fallback
        }
      );
    } else {
      handleSearch('Campo Grande');
    }
  };

  const handleSearchByCoords = async (lat, lon) => {
    setLoading(true);
    setError('');
    try {
      const response = await fetch(`http://localhost:8080/api/weather/coords?lat=${lat}&lon=${lon}`);
      if (!response.ok) throw new Error('Location not found');
      const data = await response.json();
      setWeatherData(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchFields = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/fields');
      const data = await response.json();
      if (data.length > 0) setActiveField(data[0]);
    } catch (err) {
      console.error("Failed to fetch fields", err);
    }
  };

  const handleSearch = async (searchCity) => {
    const targetCity = searchCity || city;
    if (!targetCity.trim()) return;

    setLoading(true);
    setError('');

    try {
      const response = await fetch(`http://localhost:8080/api/weather/${targetCity}`);
      if (!response.ok) throw new Error('Location not found');
      const data = await response.json();
      setWeatherData(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    handleSearch();
  };

  return (
    <div className="app-container">
      {/* Search Overlay */}
      <div className="top-overlay">
        <form className="strategic-search" onSubmit={handleSubmit}>
          <div className="search-box">
            <span className="search-icon">🔍</span>
            <input
              type="text"
              value={city}
              onChange={(e) => setCity(e.target.value)}
              placeholder="Search Strategic Region..."
              className="search-input"
            />
            <button type="submit" className="search-submit" disabled={loading}>
              {loading ? '...' : 'ANALYSIS'}
            </button>
          </div>
          {error && <div className="search-error">{error}</div>}
        </form>
      </div>

      {/* Main Map Background */}
      <FullMap weather={weatherData} />

      {/* Right Sidebar */}
      <Sidebar weather={weatherData} field={activeField} />

      {/* Bottom Analytics */}
      <BottomPanel field={activeField} />

      {/* Loading Overlay */}
      {loading && (
        <div className="global-loader">
          <div className="loader-ring"></div>
          <span>Processing Satellite Data...</span>
        </div>
      )}
    </div>
  );
}

export default App;
