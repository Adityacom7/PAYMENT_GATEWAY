import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Header = () => {
  const location = useLocation();

  return (
    <header className="header">
      <div className="container">
        <nav className="nav">
          <div className="logo">PayPlus</div>
          <div className="nav-links">
            <Link to="/" className={location.pathname === '/' ? 'active' : ''}>
              Dashboard
            </Link>
            <Link to="/payment" className={location.pathname === '/payment' ? 'active' : ''}>
              Process Payment
            </Link>
            <Link to="/tokenization" className={location.pathname === '/tokenization' ? 'active' : ''}>
              Tokenization
            </Link>
            <Link to="/authorization" className={location.pathname === '/authorization' ? 'active' : ''}>
              Authorization
            </Link>
          </div>
        </nav>
      </div>
    </header>
  );
};

export default Header;