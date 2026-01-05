import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

const Dashboard = () => {
  const { logout } = useAuth();

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h1>Admin Dashboard</h1>
        <button onClick={logout}>Logout</button>
      </div>
      
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '20px' }}>
        <Link to="/admin/products" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
            <h2>Products</h2>
            <p>Manage products</p>
          </div>
        </Link>
        
        <Link to="/admin/categories" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
            <h2>Categories</h2>
            <p>Manage categories</p>
          </div>
        </Link>
        
        <Link to="/admin/brands" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
            <h2>Brands</h2>
            <p>Manage brands</p>
          </div>
        </Link>
        
        <Link to="/admin/werehouses" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
            <h2>Werehouses</h2>
            <p>Manage werehouses</p>
          </div>
        </Link>
        
        <Link to="/admin/stock" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
            <h2>Stock Management</h2>
            <p>Manage stock transactions</p>
          </div>
        </Link>
      </div>
    </div>
  );
};

export default Dashboard;

