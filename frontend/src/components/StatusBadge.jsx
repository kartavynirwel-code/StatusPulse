import React from 'react';

export function StatusBadge({ status }) {
  const isUp = status === 'UP';
  
  return (
    <div className={`status-badge ${isUp ? 'up' : 'down'}`}>
      <span className="pulse-dot"></span>
      <span>{status || 'UNKNOWN'}</span>
    </div>
  );
}
