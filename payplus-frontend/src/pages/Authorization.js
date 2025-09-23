import React, { useState } from 'react';
import axios from 'axios';

const Authorization = () => {
  const [authData, setAuthData] = useState({
    username: '',
    password: ''
  });
  const [loading, setLoading] = useState(false);
  const [authResult, setAuthResult] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await axios.post('/api/authorize', authData);
      setAuthResult({ type: 'success', message: 'Authorization successful!', data: response.data });
    } catch (error) {
      setAuthResult({ type: 'error', message: 'Authorization failed' });
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setAuthData({
      ...authData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div>
      <h1>Authorization Service</h1>
      <div className="card">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={authData.username}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={authData.password}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? 'Authorizing...' : 'Authorize'}
          </button>
        </form>

        {authResult && (
          <div className={authResult.type === 'success' ? 'success' : 'error'}>
            {authResult.message}
          </div>
        )}
      </div>
    </div>
  );
};

export default Authorization;