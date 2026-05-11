import React from 'react';
import './AgricultureAdvice.css';

/**
 * AgricultureAdvice component for displaying farming recommendations.
 * Provides simple advice based on weather conditions.
 */
const AgricultureAdvice = ({ weather }) => {
  // Generate advice based on weather conditions
  const generateAdvice = () => {
    const advice = [];
    
    // Check humidity > 80: disease risk warning
    if (weather.humidity > 80) {
      advice.push({
        type: 'humidity',
        icon: '🦠',
        message: 'High humidity detected. Check crops for disease risk.'
      });
    }
    
    // Check temperature > 35: irrigation advice
    if (weather.temperature > 35) {
      advice.push({
        type: 'temperature',
        icon: '💧',
        message: 'Very hot temperature. Water crops early morning or evening.'
      });
    }
    
    // Check windSpeed > 8: protect young plants
    if (weather.windSpeed > 8) {
      advice.push({
        type: 'wind',
        icon: '🌱',
        message: 'Strong wind detected. Protect young plants.'
      });
    }
    
    // Otherwise: normal farming advice
    if (advice.length === 0) {
      advice.push({
        type: 'normal',
        icon: '✅',
        message: 'Normal farming conditions. Weather looks good for agricultural activities.'
      });
    }
    
    return advice;
  };

  const adviceList = generateAdvice();

  return (
    <div className="agriculture-advice">
      <h3 className="advice-title">🌾 Agriculture Advice</h3>
      
      <div className="advice-list">
        {adviceList.map((advice, index) => (
          <div key={index} className={`advice-item ${advice.type}`}>
            <div className="advice-icon">{advice.icon}</div>
            <div className="advice-message">{advice.message}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AgricultureAdvice;
