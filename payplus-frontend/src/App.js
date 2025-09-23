import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      setPayments([
        { id: 1, amount: 100.00, status: 'Completed', date: '2024-01-15' },
        { id: 2, amount: 250.50, status: 'Pending', date: '2024-01-16' },
        { id: 3, amount: 75.25, status: 'Completed', date: '2024-01-14' }
      ]);
      setLoading(false);
    }, 1000);
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <h1>PayPlus Frontend</h1>
        <p>Modern Payment Solution</p>
      </header>

      <main className="App-main">
        <section className="payments-section">
          <h2>Recent Payments</h2>
          {loading ? (
            <p>Loading payments...</p>
          ) : (
            <div className="payments-list">
              {payments.map(payment => (
                <div key={payment.id} className="payment-card">
                  <div className="payment-info">
                    <span className="amount">${payment.amount}</span>
                    <span className={`status ${payment.status.toLowerCase()}`}>
                      {payment.status}
                    </span>
                  </div>
                  <div className="payment-date">{payment.date}</div>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;