import React from 'react';
import './Sidebar.css';

const Sidebar = ({ weather, field }) => {
  if (!weather) return null;

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <div className="field-info">
          <div className="field-icon">⬢</div>
          <div className="field-details">
            <h3>{field?.name || 'Loading Field...'}</h3>
            <p>{field?.area || '0'} ha</p>
            <span className="coords">{field?.latitude?.toFixed(4)}° S {field?.longitude?.toFixed(4)}° W</span>
          </div>
          <div className="header-actions">
            <button className="icon-btn">🗑</button>
            <button className="icon-btn">✎</button>
          </div>
        </div>
      </div>

      <div className="sidebar-content">
        <section className="weather-section">
          <div className="section-header">
            <h4>Weather today</h4>
            <span className="toggle-icon">▲</span>
          </div>
          
          <div className="weather-main">
            <div className="weather-desc">
              <span className="condition">{weather.weather?.[0]?.description || 'Cloudy'}</span>
              <span className="date">{new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}</span>
            </div>
            <div className="temp-display">
              <span className="temp">{Math.round(weather.main?.temp || 0)}°</span>
              <span className="weather-icon">☁</span>
            </div>
          </div>

          <div className="weather-stats">
            <div className="stat">
              <label>Wind</label>
              <span>{weather.wind?.speed} m/s ↙</span>
            </div>
            <div className="stat">
              <label>Humidity</label>
              <span>{weather.main?.humidity}%</span>
            </div>
            <div className="stat">
              <label>Clouds</label>
              <span>{weather.clouds?.all}%</span>
            </div>
          </div>

          <div className="precipitation">
            <div className="stat">
              <label>Precipitation</label>
              <span>0 mm</span>
            </div>
            <button className="forecast-link">Forecast</button>
          </div>
        </section>

        <section className="tasks-section">
          <div className="section-header">
            <h4>Scouting Tasks</h4>
            <span className="info-icon">ⓘ</span>
          </div>
          
          <div className="tabs">
            <button className="tab active">New (0)</button>
            <button className="tab">Closed (0)</button>
          </div>

          <div className="empty-state">
            <p>You have no new tasks</p>
            <ol>
              <li>Click <span>Add new task</span> and drop a pin on a map within your field area.</li>
              <li>To complete the task, follow the link in the sms and install the mobile app.</li>
            </ol>
          </div>

          <button className="action-btn secondary">📱 GET THE LINK</button>
          <div className="app-badges">
            <img src="https://upload.wikimedia.org/wikipedia/commons/7/78/Google_Play_Store_badge_EN.svg" alt="Play Store" />
          </div>
          <button className="action-btn primary">+ ADD NEW TASK</button>
        </section>
      </div>
    </aside>
  );
};

export default Sidebar;
