975050277554.dkr.ecr.ap-south-1.amazonaws.com/payplus/authorization-service:1.0


.App {
  text-align: center;
  min-height: 100vh;
}

.App-header {
  background-color: #282c34;
  padding: 2rem;
  color: white;
}

.App-header h1 {
  margin: 0 0 0.5rem 0;
  font-size: 2.5rem;
}

.App-header p {
  margin: 0;
  opacity: 0.8;
}

.App-main {
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

.payments-section h2 {
  color: #333;
  margin-bottom: 1.5rem;
}

.payments-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.payment-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.payment-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 0.5rem;
}

.amount {
  font-size: 1.5rem;
  font-weight: bold;
  color: #2c5aa0;
}

.status {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.875rem;
  font-weight: 500;
}

.status.completed {
  background-color: #d4edda;
  color: #155724;
}

.status.pending {
  background-color: #fff3cd;
  color: #856404;
}

.payment-date {
  color: #666;
  font-size: 0.9rem;
}