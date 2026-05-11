import React, { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import './MapCard.css';

const FullMap = ({ weather }) => {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);

  useEffect(() => {
    if (!mapRef.current) return;

    // Center on field if weather is available, else default
    const lat = weather?.latitude || -26.6190;
    const lon = weather?.longitude || -55.1699;

    if (!mapInstanceRef.current) {
      // Initialize map
      const map = L.map(mapRef.current, {
        zoomControl: false, // Custom position handled in CSS
      }).setView([lat, lon], 14);

      // Add Satellite Imagery (Esri)
      L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
        attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
      }).addTo(map);

      // Add Zoom Control to the left
      L.control.zoom({ position: 'topleft' }).addTo(map);

      // Draw a mock polygon for the field
      const polygonPoints = [
        [lat + 0.005, lon - 0.005],
        [lat + 0.006, lon + 0.005],
        [lat - 0.003, lon + 0.008],
        [lat - 0.005, lon - 0.002]
      ];
      L.polygon(polygonPoints, {
        color: '#fbbf24',
        fillColor: '#22c55e',
        fillOpacity: 0.4,
        weight: 2
      }).addTo(map);

      mapInstanceRef.current = map;
    } else {
      mapInstanceRef.current.setView([lat, lon]);
    }

    return () => {
      // Don't remove map to keep it persistent as background
    };
  }, [weather]);

  return (
    <div className="full-map-wrapper">
      <div ref={mapRef} className="background-map" />
      
      {/* Left Toolbar overlay */}
      <div className="map-toolbar">
        <button className="tool-btn">🎚</button>
        <button className="tool-btn">📏</button>
        <button className="tool-btn">📍</button>
        <button className="tool-btn">🗺</button>
      </div>

      {/* Map Bottom Stats Overlay */}
      <div className="map-stats-overlay">
        <div className="stat-pill">
          <label>Clouds</label>
          <span>0 ha / 0%</span>
        </div>
      </div>
    </div>
  );
};

export default FullMap;
