import React, { useState, useEffect } from 'react';
import './BottomPanel.css';

const BottomPanel = ({ field }) => {
  const [analytics, setAnalytics] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (field?.id) {
      fetchAnalytics(field.id);
    }
  }, [field]);

  const fetchAnalytics = async (fieldId) => {
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/fields/${fieldId}/analytics`);
      const data = await response.json();
      setAnalytics(data);
    } catch (err) {
      console.error("Failed to fetch analytics", err);
    } finally {
      setLoading(false);
    }
  };

  // Helper to map values to SVG coordinates
  const getNDVIPath = () => {
    if (analytics.length < 2) return "";
    const width = 800;
    const height = 150;
    const step = width / (analytics.length - 1);
    
    return analytics.map((d, i) => {
      const x = i * step;
      const y = height - (d.ndviValue * height); // NDVI is 0-1
      return `${i === 0 ? 'M' : 'L'}${x},${y}`;
    }).join(' ');
  };

  return (
    <div className="bottom-panel">
      {/* Timeline Strip */}
      <div className="timeline-strip">
        <button className="nav-btn">❮</button>
        <div className="timeline-items">
          {analytics.map((item, i) => (
            <div key={i} className={`timeline-item ${i === analytics.length - 1 ? 'active' : ''}`}>
              <span className="cloud-icon">☁</span>
              <span className="date">{new Date(item.date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}</span>
            </div>
          ))}
        </div>
        <button className="nav-btn">❯</button>
      </div>

      {/* Analytics Dashboard */}
      <div className="analytics-dashboard">
        <div className="analytics-controls">
          <div className="control-group">
            <label>Index</label>
            <select><option>NDVI</option></select>
          </div>
          <div className="control-group">
            <label>Start date</label>
            <div className="date-input">{analytics[0] ? new Date(analytics[0].date).toLocaleDateString() : '...'} 📅</div>
          </div>
          <div className="control-group">
            <label>End date</label>
            <div className="date-input">{analytics[analytics.length-1] ? new Date(analytics[analytics.length-1].date).toLocaleDateString() : '...'} 📅</div>
          </div>
          <div className="control-group">
            <label>Weather Data</label>
            <select><option>Moisture</option></select>
          </div>
        </div>

        <div className="chart-container">
          <div className="chart-header">
            <div className="legend">
              <span className="legend-item ndvi">NDVI (Real)</span>
              <span className="legend-item year-2021">Current Season</span>
              <span className="legend-item precip">Precip, mm</span>
            </div>
            <div className="chart-actions">
              <button className="icon-btn">⤓</button>
              <button className="icon-btn">⛶</button>
            </div>
          </div>
          
          <div className="svg-chart-wrapper">
            {loading ? (
              <div className="chart-loader">Syncing Satellite Analytics...</div>
            ) : (
              <svg viewBox="0 0 800 150" preserveAspectRatio="none">
                {/* Real NDVI Line */}
                <path d={getNDVIPath()} fill="none" stroke="#22c55e" strokeWidth="2" />
                
                {/* Real Precipitation Bars */}
                {analytics.map((d, i) => {
                  const step = 800 / analytics.length;
                  const h = (d.precipitation / 120) * 150; // Normalize precip
                  return (
                    <rect 
                      key={i}
                      x={i * step + (step * 0.2)} 
                      y={150 - h} 
                      width={step * 0.6} 
                      height={h} 
                      fill="#38bdf8" 
                      opacity="0.5" 
                    />
                  );
                })}
              </svg>
            )}
          </div>
          
          <div className="chart-footer">
            {analytics.map((d, i) => (
              <span key={i}>{new Date(d.date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}</span>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default BottomPanel;
