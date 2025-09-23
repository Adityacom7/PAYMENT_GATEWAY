import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalPayments: 0,
    successfulPayments: 0,
    failedPayments: 0,
    tokensCreated: 0
  });

  useEffect(() => {
    // Fetch dashboard statistics
    const fetchStats = async () => {
      try {
        // Replace with actual API endpoints
        const response = await axios.get('/api/dashboard/stats');
        setStats(response.data);
      } catch (error) {
        console.error('Error fetching dashboard stats:', error);
      }
    };

    fetchStats();
  }, []);

  return (
    <div>
      <h1>Dashboard</h1>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1rem' }}>
        <div className="card">
          <h3>Total Payments</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#667eea' }}>{stats.totalPayments}</p>
        </div>
        <div className="card">
          <h3>Successful Payments</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#28a745' }}>{stats.successfulPayments}</p>
        </div>
        <div className="card">
          <h3>Failed Payments</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#dc3545' }}>{stats.failedPayments}</p>
        </div>
        <div className="card">
          <h3>Tokens Created</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#ffc107' }}>{stats.tokensCreated}</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;