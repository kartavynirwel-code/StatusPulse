import React from 'react';
import { Activity, LayoutDashboard, History, Plus, RefreshCw } from 'lucide-react';

export function Header({ activeTab, setActiveTab, onOpenAddModal, lastUpdated, isRefreshing, onManualRefresh }) {
  return (
    <header className="app-header">
      <div className="brand">
        <div className="brand-icon">
          <Activity size={24} />
        </div>
        <div>
          <h1 className="brand-title">StatusPulse</h1>
          <p className="brand-tagline">Real-Time Infrastructure & Service Health</p>
        </div>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
        <div className="nav-tabs">
          <button
            className={`tab-btn ${activeTab === 'dashboard' ? 'active' : ''}`}
            onClick={() => setActiveTab('dashboard')}
          >
            <LayoutDashboard size={16} />
            <span>Dashboard</span>
          </button>
          <button
            className={`tab-btn ${activeTab === 'timeline' ? 'active' : ''}`}
            onClick={() => setActiveTab('timeline')}
          >
            <History size={16} />
            <span>Incident Timeline</span>
          </button>
        </div>

        <button
          className="action-btn"
          onClick={onManualRefresh}
          title="Refresh Data"
          disabled={isRefreshing}
          style={{ padding: '0.5rem 0.75rem', background: 'rgba(255,255,255,0.05)' }}
        >
          <RefreshCw size={16} className={isRefreshing ? 'animate-spin' : ''} />
        </button>

        <button className="btn-primary" onClick={onOpenAddModal} style={{ width: 'auto' }}>
          <Plus size={18} />
          <span>Add Service</span>
        </button>
      </div>
    </header>
  );
}
