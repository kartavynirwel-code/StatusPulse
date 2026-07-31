import React, { useState } from 'react';
import { X, Plus, AlertCircle } from 'lucide-react';

export function AddServiceModal({ isOpen, onClose, onAddService }) {
  const [name, setName] = useState('');
  const [url, setUrl] = useState('');
  const [intervalSeconds, setIntervalSeconds] = useState(10);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!name.trim() || !url.trim()) {
      setError('Please provide both service name and valid endpoint URL.');
      return;
    }

    if (intervalSeconds < 1) {
      setError('Check interval must be at least 1 second.');
      return;
    }

    try {
      setIsSubmitting(true);
      await onAddService({
        name: name.trim(),
        url: url.trim(),
        intervalSeconds: Number(intervalSeconds),
      });
      setName('');
      setUrl('');
      setIntervalSeconds(10);
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to register service. Check backend connectivity.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 600 }}>Add Monitored Service</h2>
          <button
            onClick={onClose}
            style={{ background: 'none', border: 'none', color: '#9ca3af', cursor: 'pointer' }}
          >
            <X size={20} />
          </button>
        </div>

        {error && (
          <div style={{
            background: 'rgba(239, 68, 68, 0.15)',
            border: '1px solid rgba(239, 68, 68, 0.3)',
            color: '#ef4444',
            padding: '0.75rem',
            borderRadius: '8px',
            marginBottom: '1.25rem',
            fontSize: '0.85rem',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem'
          }}>
            <AlertCircle size={16} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Service Name</label>
            <input
              type="text"
              className="form-control"
              placeholder="e.g. Auth Microservice, Payment API"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Target URL Endpoint</label>
            <input
              type="url"
              className="form-control"
              placeholder="https://api.example.com/health"
              value={url}
              onChange={(e) => setUrl(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Check Interval (seconds)</label>
            <input
              type="number"
              min="1"
              max="3600"
              className="form-control"
              value={intervalSeconds}
              onChange={(e) => setIntervalSeconds(e.target.value)}
              required
            />
          </div>

          <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1.5rem' }}>
            <button
              type="button"
              className="action-btn"
              onClick={onClose}
              style={{ flex: 1, padding: '0.75rem', justifyContent: 'center' }}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn-primary"
              disabled={isSubmitting}
              style={{ flex: 2 }}
            >
              <Plus size={18} />
              <span>{isSubmitting ? 'Registering...' : 'Add Service'}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
