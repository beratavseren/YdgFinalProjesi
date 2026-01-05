import React, { useState, useEffect } from 'react';
import { workerLandingAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Landing = () => {
  const { logout } = useAuth();
  const [landingInfo, setLandingInfo] = useState(null);
  const [products, setProducts] = useState([]);
  const [criticalStock, setCriticalStock] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [info, productsData, criticalData, transactionsData] = await Promise.all([
        workerLandingAPI.getWerehouseWorkerLandingInfo(),
        workerLandingAPI.getProducts(),
        workerLandingAPI.getCriticalStockCount(),
        workerLandingAPI.getTransactions(),
      ]);
      setLandingInfo(info);
      setProducts(productsData);
      setCriticalStock(criticalData);
      setTransactions(transactionsData);
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h1>Worker Dashboard</h1>
        <div>
          <a href="/worker/profile" style={{ marginRight: '20px' }}>Profile</a>
          <a href="/worker/stock" style={{ marginRight: '20px' }}>Stock</a>
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      {landingInfo && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px', marginBottom: '30px' }}>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px' }}>
            <h3>Total Quantity</h3>
            <p style={{ fontSize: '24px', fontWeight: 'bold' }}>{landingInfo.totalQuantity}</p>
          </div>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px' }}>
            <h3>Current Weight</h3>
            <p style={{ fontSize: '24px', fontWeight: 'bold' }}>{landingInfo.currentWeigth} / {landingInfo.weigthLimit}</p>
          </div>
          <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px' }}>
            <h3>Critical Level Products</h3>
            <p style={{ fontSize: '24px', fontWeight: 'bold', color: 'red' }}>{landingInfo.criticalLevelProductQuantity}</p>
          </div>
        </div>
      )}

      <div style={{ marginBottom: '30px' }}>
        <h2>Critical Stock Products</h2>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ borderBottom: '2px solid #ccc' }}>
              <th style={{ padding: '10px', textAlign: 'left' }}>Product</th>
              <th style={{ padding: '10px', textAlign: 'left' }}>Quantity</th>
            </tr>
          </thead>
          <tbody>
            {criticalStock.map((item, index) => (
              <tr key={index} style={{ borderBottom: '1px solid #eee' }}>
                <td style={{ padding: '10px' }}>{item.productName || 'N/A'}</td>
                <td style={{ padding: '10px', color: 'red' }}>{item.quantity}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div style={{ marginBottom: '30px' }}>
        <h2>Recent Transactions</h2>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ borderBottom: '2px solid #ccc' }}>
              <th style={{ padding: '10px', textAlign: 'left' }}>Transaction ID</th>
              <th style={{ padding: '10px', textAlign: 'left' }}>Type</th>
              <th style={{ padding: '10px', textAlign: 'left' }}>Werehouse</th>
            </tr>
          </thead>
          <tbody>
            {transactions.slice(0, 10).map(transaction => (
              <tr key={transaction.transactionId} style={{ borderBottom: '1px solid #eee' }}>
                <td style={{ padding: '10px' }}>{transaction.transactionId}</td>
                <td style={{ padding: '10px' }}>{transaction.transactionType}</td>
                <td style={{ padding: '10px' }}>{transaction.werehouseName}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Landing;

