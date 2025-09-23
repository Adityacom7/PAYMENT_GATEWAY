import React, { useState } from 'react';
import axios from 'axios';

const Tokenization = () => {
  const [cardData, setCardData] = useState({
    cardNumber: '',
    expiryDate: '',
    cvv: ''
  });
  const [loading, setLoading] = useState(false);
  const [token, setToken] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await axios.post('/api/tokenize', cardData);
      setToken(response.data.token);
    } catch (error) {
      console.error('Tokenization failed:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setCardData({
      ...cardData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div>
      <h1>Tokenization Service</h1>
      <div className="card">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Card Number</label>
            <input
              type="text"
              name="cardNumber"
              value={cardData.cardNumber}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Expiry Date</label>
            <input
              type="text"
              name="expiryDate"
              value={cardData.expiryDate}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>CVV</label>
            <input
              type="text"
              name="cvv"
              value={cardData.cvv}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? 'Tokenizing...' : 'Tokenize Card'}
          </button>
        </form>

        {token && (
          <div className="success">
            <h3>Token Created Successfully!</h3>
            <p>Token: <strong>{token}</strong></p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Tokenization;    