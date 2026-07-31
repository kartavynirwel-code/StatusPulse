import React, { useState } from 'react';
import { History, Filter, AlertTriangle, CheckCircle2, Clock } from 'lucide-react';
import { StatusBadge } from './StatusBadge';

export function IncidentTimeline({ history, services }) {
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [serviceFilter, setServiceFilter] = useState('ALL');

  const getServiceName = (serviceId) => {
    const s = services.find((srv) => srv.id === serviceId);
    return s ? s.name : `Service #${serviceId}`;
  };

  const filteredHistory = history.filter((item) => {
    const matchesStatus = statusFilter === 'ALL' || item.status === statusFilter;
    const matchesService = serviceFilter === 'ALL' || String(item.serviceId) === String(serviceFilter);
    return matchesStatus && matchesService;
  });

  const formatTimestamp = (isoStr) => {
    if (!isoStr) return '';
    const date = new Date(isoStr);
    return date.toLocaleString([], {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });
  };

  return (
    <div>
      {/* Controls */}
      <div className="controls-bar">
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <History size={20} style={{ color: 'var(--accent-blue)' }} />
          <h2 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Incident & Check Timeline</h2>
        </div>

        <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
          {/* Status Filter */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', background: 'rgba(0,0,0,0.3)', padding: '0.25rem 0.5rem', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
            <Filter size={14} style={{ color: 'var(--text-secondary)' }} />
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              style={{ background: 'transparent', border: 'none', color: 'var(--text-primary)', outline: 'none', cursor: 'pointer', fontSize: '0.85rem' }}
            >
              <option value="ALL" style={{ background: '#111827' }}>All Statuses</option>
              <option value="DOWN" style={{ background: '#111827' }}>DOWN Incidents Only</option>
              <option value="UP" style={{ background: '#111827' }}>UP Checks Only</option>
            </select>
          </div>

          {/* Service Filter */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', background: 'rgba(0,0,0,0.3)', padding: '0.25rem 0.5rem', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
            <select
              value={serviceFilter}
              onChange={(e) => setServiceFilter(e.target.value)}
              style={{ background: 'transparent', border: 'none', color: 'var(--text-primary)', outline: 'none', cursor: 'pointer', fontSize: '0.85rem' }}
            >
              <option value="ALL" style={{ background: '#111827' }}>All Services</option>
              {services.map((srv) => (
                <option key={srv.id} value={srv.id} style={{ background: '#111827' }}>
                  {srv.name}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Timeline Stream */}
      {filteredHistory.length === 0 ? (
        <div className="empty-state glass-card">
          <History className="empty-icon" />
          <h3>No Status Check Events</h3>
          <p style={{ marginTop: '0.5rem', fontSize: '0.85rem' }}>
            No matching check logs found. Registered services will automatically generate status checks based on their interval.
          </p>
        </div>
      ) : (
        <div className="timeline-list">
          {filteredHistory.map((item) => {
            const isUp = item.status === 'UP';
            return (
              <div key={item.id} className="timeline-item">
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  <div style={{
                    padding: '0.5rem',
                    borderRadius: '8px',
                    background: isUp ? 'var(--accent-up-glow)' : 'var(--accent-down-glow)',
                    color: isUp ? 'var(--accent-up)' : 'var(--accent-down)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                  }}>
                    {isUp ? <CheckCircle2 size={18} /> : <AlertTriangle size={18} />}
                  </div>

                  <div>
                    <div style={{ fontWeight: 600, fontSize: '0.95rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                      <span>{getServiceName(item.serviceId)}</span>
                      <StatusBadge status={item.status} />
                    </div>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginTop: '0.2rem', display: 'flex', gap: '1rem' }}>
                      <span>Latency: {item.responseTimeMs != null ? `${item.responseTimeMs} ms` : 'N/A'}</span>
                      <span>Service ID: #{item.serviceId}</span>
                    </div>
                  </div>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                  <Clock size={14} />
                  <span>{formatTimestamp(item.checkedAt)}</span>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
