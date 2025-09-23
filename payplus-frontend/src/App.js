import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Dashboard from './pages/Dashboard';
import Payment from './pages/Payment';
import Tokenization from './pages/Tokenization';
import Authorization from './pages/Authorization';
import './App.css';

function App() {
  return (
    <div className="App">
      <Header />
      <main className="main-content">
        <div className="container">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/payment" element={<Payment />} />
            <Route path="/tokenization" element={<Tokenization />} />
            <Route path="/authorization" element={<Authorization />} />
          </Routes>
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default App;