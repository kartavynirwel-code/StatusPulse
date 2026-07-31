import React from 'react';
import { ExternalLink, Trash2, Clock, Zap, ShieldCheck } from 'lucide-react';
import { StatusBadge } from './StatusBadge';

export function ServiceCard({ service, onDelete }) {
  const formatTime = (isoString) => {
    if (!isoString) return 'Never checked';
    const date = new Date(isoString);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  };

  const getUptimeColor = (pct) => {
    if (pct >= 99) return '#10b981';
    if (pct >= 95) return '#f59e0b';
    return '#ef4444';
  };

  return (
    <div className="glass-card">
      <div className="service-card-header">
        <div>
          <h3 className="service-name">{service.name}</h3>
          <a
            href={service.url}
            target="_blank"
            rel="noopener noreferrer"
            className="service-url"
          >
            {service.url} <ExternalLink size={12} />
          </a>
        </div>
        <StatusBadge status={service.currentStatus} />
      </div>

      <div className="service-card-body">
        <div className="metric-item">
          <span className="metric-label" style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
            <ShieldCheck size={12} /> Uptime
          </span>
          <span
            className="metric-val"
            style={{ color: getUptimeColor(service.uptimePercentage) }}
          >
            {service.uptimePercentage != null ? `${service.uptimePercentage}%` : '100%'}
          </span>
        </div>

        <div className="metric-item">
          <span className="metric-label" style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
            <Zap size={12} /> Latency
          </span>
          <span className="metric-val">
            {service.lastResponseTimeMs != null ? `${service.lastResponseTimeMs} ms` : 'N/A'}
          </span>
        </div>

        <div className="metric-item">
          <span className="metric-label" style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
            <Clock size={12} /> Interval
          </span>
          <span className="metric-val">{service.intervalSeconds}s</span>
        </div>

        <div className="metric-item">
          <span className="metric-label">Total Checks</span>
          <span className="metric-val">{service.totalChecks || 0}</span>
        </div>
      </div>

      <div className="service-card-footer">
        <span>Last check: {formatTime(service.lastCheckedAt)}</span>
        <button
          className="action-btn"
          onClick={() => onDelete(service.id, service.name)}
          title="Delete service"
        >
          <Trash2 size={14} /> Delete
        </button>
      </div>
    </div>
  );
}
