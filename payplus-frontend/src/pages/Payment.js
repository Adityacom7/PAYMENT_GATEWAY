import React, { useState } from 'react';
import axios from 'axios';

const Payment = () => {
  const [paymentData, setPaymentData] = useState({
    amount: '',
    currency: 'USD',
    cardNumber: '',
    expiryDate: '',
    cvv: '',
    description: ''
  });
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setResult(null);

    try {
      const response = await axios.post('/api/process-payment', paymentData);
      setResult({ type: 'success', message: 'Payment processed successfully!', data: response.data });
    } catch (error) {
      setResult({ type: 'error', message: 'Payment failed: ' + (error.response?.data?.message || error.message) });
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setPaymentData({
      ...paymentData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div>
      <h1>Process Payment</h1>
      <div className="card">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Amount</label>
            <input
              type="number"
              name="amount"
              value={paymentData.amount}
              onChange={handleChange}
              required
              step="0.01"
            />
          </div>
          
          <div className="form-group">
            <label>Currency</label>
            <select name="currency" value={paymentData.currency} onChange={handleChange}>
              <option value="USD">USD</option>
              <option value="EUR">EUR</option>
              <option value="GBP">GBP</option>
            </select>
          </div>

          <div className="form-group">
            <label>Card Number</label>
            <input
              type="text"
              name="cardNumber"
              value={paymentData.cardNumber}
              onChange={handleChange}
              required
              placeholder="1234 5678 9012 3456"
            />
          </div>

          <div className="form-group">
            <label>Expiry Date</label>
            <input
              type="text"
              name="expiryDate"
              value={paymentData.expiryDate}
              onChange={handleChange}
              required
              placeholder="MM/YY"
            />
          </div>

          <div className="form-group">
            <label>CVV</label>
            <input
              type="text"
              name="cvv"
              value={paymentData.cvv}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <input
              type="text"
              name="description"
              value={paymentData.description}
              onChange={handleChange}
            />
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? 'Processing...' : 'Process Payment'}
          </button>
        </form>

        {result && (
          <div className={result.type === 'success' ? 'success' : 'error'}>
            {result.message}
          </div>
        )}
      </div>
    </div>
  );
};

export default Payment;