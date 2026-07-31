/**
 * StatusPulse API Client
 * Configurable base URL via VITE_API_BASE_URL environment variable.
 */
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

async function request(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  const defaultHeaders = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };

  const config = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers,
    },
  };

  try {
    const response = await fetch(url, config);
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP Error ${response.status}: ${errorText || response.statusText}`);
    }
    if (response.status === 244 || response.status === 204) {
      return null;
    }
    return await response.json();
  } catch (err) {
    console.error(`[API Error] Request to ${url} failed:`, err);
    throw err;
  }
}

export const api = {
  // Get live current status of all monitored services
  getServiceStatuses: () => request('/api/services/status/current'),

  // Get raw list of registered monitored services
  getAllServices: () => request('/api/services'),

  // Register a new service
  createService: (serviceData) => request('/api/services', {
    method: 'POST',
    body: JSON.stringify(serviceData),
  }),

  // Update a monitored service
  updateService: (id, serviceData) => request(`/api/services/${id}`, {
    method: 'PUT',
    body: JSON.stringify(serviceData),
  }),

  // Delete a monitored service
  deleteService: (id) => request(`/api/services/${id}`, {
    method: 'DELETE',
  }),

  // Get incident history for a specific service
  getServiceHistory: (id, limit = 50) => request(`/api/services/${id}/history?limit=${limit}`),

  // Get global status check history for incident timeline
  getRecentHistory: () => request('/api/services/history/recent'),
};
