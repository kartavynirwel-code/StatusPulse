import React, { useState, useEffect, useCallback } from 'react';
import { Header } from './components/Header';
import { ServiceCard } from './components/ServiceCard';
import { AddServiceModal } from './components/AddServiceModal';
import { IncidentTimeline } from './components/IncidentTimeline';
import { api } from './services/api';
import { Server, CheckCircle2, XCircle, Activity, Search, RefreshCw, AlertCircle } from 'lucide-react';

export default function App() {
  const [services, setServices] = useState([]);
  const [history, setHistory] = useState([]);
  const [activeTab, setActiveTab] = useState('dashboard');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [lastUpdated, setLastUpdated] = useState(null);
  const [apiError, setApiError] = useState(null);

  const fetchData = useCallback(async (showLoader = false) => {
    if (showLoader) setIsRefreshing(true);
    try {
      const [statusData, historyData] = await Promise.all([
        api.getServiceStatuses(),
        api.getRecentHistory(),
      ]);
      setServices(statusData || []);
      setHistory(historyData || []);
      setLastUpdated(new Date());
      setApiError(null);
    } catch (err) {
      console.error('Error fetching StatusPulse data:', err);
      setApiError('Unable to connect to monitor-service backend at ' + (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'));
    } finally {
      setIsRefreshing(false);
    }
  }, []);

  // Poll API every 5 seconds for live status updates
  useEffect(() => {
    fetchData(true);
    const interval = setInterval(() => {
      fetchData(false);
    }, 5000);
    return () => clearInterval(interval);
  }, [fetchData]);

  const handleAddService = async (serviceData) => {
    await api.createService(serviceData);
    fetchData(true);
  };

  const handleDeleteService = async (id, name) => {
    if (window.confirm(`Are you sure you want to stop monitoring '${name}'?`)) {
      try {
        await api.deleteService(id);
        fetchData(true);
      } catch (err) {
        alert(`Failed to delete service: ${err.message}`);
      }
    }
  };

  // Metrics calculations
  const totalServices = services.length;
  const upCount = services.filter((s) => s.currentStatus === 'UP').length;
  const downCount = services.filter((s) => s.currentStatus === 'DOWN').length;
  const avgUptime = totalServices > 0
    ? (services.reduce((acc, s) => acc + (s.uptimePercentage || 100), 0) / totalServices).toFixed(2)
    : '100.00';

  const filteredServices = services.filter(
    (s) =>
      s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      s.url.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="app-container">
      <Header
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onOpenAddModal={() => setIsAddModalOpen(true)}
        lastUpdated={lastUpdated}
        isRefreshing={isRefreshing}
        onManualRefresh={() => fetchData(true)}
      />

      {apiError && (
        <div style={{
          background: 'rgba(239, 68, 68, 0.12)',
          border: '1px solid rgba(239, 68, 68, 0.3)',
          color: '#f87171',
          padding: '1rem 1.25rem',
          borderRadius: '12px',
          marginBottom: '2rem',
          display: 'flex',
          alignItems: 'center',
          gap: '0.75rem'
        }}>
          <AlertCircle size={20} />
          <div>
            <strong>Backend Connection Warning:</strong> {apiError}
            <div style={{ fontSize: '0.8rem', color: '#9ca3af', marginTop: '0.2rem' }}>
              Ensure `monitor-service` is running on port 8080 and CORS is enabled.
            </div>
          </div>
        </div>
      )}

      {/* Top Banner Stats */}
      <div className="stats-grid">
        <div className="glass-card stat-card">
          <div className="stat-icon" style={{ color: 'var(--accent-blue)' }}>
            <Server size={22} />
          </div>
          <div>
            <div className="stat-value">{totalServices}</div>
            <div className="stat-label">Total Services</div>
          </div>
        </div>

        <div className="glass-card stat-card">
          <div className="stat-icon" style={{ color: 'var(--accent-up)' }}>
            <CheckCircle2 size={22} />
          </div>
          <div>
            <div className="stat-value" style={{ color: 'var(--accent-up)' }}>{upCount}</div>
            <div className="stat-label">Services Online</div>
          </div>
        </div>

        <div className="glass-card stat-card">
          <div className="stat-icon" style={{ color: 'var(--accent-down)' }}>
            <XCircle size={22} />
          </div>
          <div>
            <div className="stat-value" style={{ color: downCount > 0 ? 'var(--accent-down)' : 'var(--text-primary)' }}>
              {downCount}
            </div>
            <div className="stat-label">Incidents / Down</div>
          </div>
        </div>

        <div className="glass-card stat-card">
          <div className="stat-icon" style={{ color: 'var(--accent-purple)' }}>
            <Activity size={22} />
          </div>
          <div>
            <div className="stat-value">{avgUptime}%</div>
            <div className="stat-label">Avg System Uptime</div>
          </div>
        </div>
      </div>

      {/* Main Tab Content */}
      {activeTab === 'dashboard' ? (
        <div>
          {/* Controls Bar */}
          <div className="controls-bar">
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', flex: 1, maxWidth: '400px' }}>
              <div style={{ position: 'relative', width: '100%' }}>
                <Search
                  size={16}
                  style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }}
                />
                <input
                  type="text"
                  className="form-control"
                  placeholder="Filter by name or URL..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  style={{ paddingLeft: '2.4rem' }}
                />
              </div>
            </div>

            <div className="refresh-indicator">
              <RefreshCw size={12} className={isRefreshing ? 'animate-spin' : ''} />
              <span>
                Auto-refreshing every 5s • Last updated: {lastUpdated ? lastUpdated.toLocaleTimeString() : 'Loading...'}
              </span>
            </div>
          </div>

          {/* Cards Grid */}
          {filteredServices.length === 0 ? (
            <div className="empty-state glass-card">
              <Server className="empty-icon" />
              <h3>No Monitored Services Found</h3>
              <p style={{ marginTop: '0.5rem', marginBottom: '1.5rem', fontSize: '0.85rem' }}>
                {searchQuery ? 'No services match your search filter.' : 'Get started by adding your first web service endpoint or API to monitor.'}
              </p>
              {!searchQuery && (
                <button className="btn-primary" onClick={() => setIsAddModalOpen(true)} style={{ margin: '0 auto', width: 'auto' }}>
                  Add First Service
                </button>
              )}
            </div>
          ) : (
            <div className="cards-grid">
              {filteredServices.map((service) => (
                <ServiceCard
                  key={service.id}
                  service={service}
                  onDelete={handleDeleteService}
                />
              ))}
            </div>
          )}
        </div>
      ) : (
        <IncidentTimeline history={history} services={services} />
      )}

      {/* Add Service Modal */}
      <AddServiceModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onAddService={handleAddService}
      />
    </div>
  );
}
