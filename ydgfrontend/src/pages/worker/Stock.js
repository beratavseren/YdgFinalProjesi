import React, { useState, useEffect } from 'react';
import { workerStockAPI, workerLandingAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Stock = () => {
  const { logout } = useAuth();
  const [transactions, setTransactions] = useState([]);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [transactionDetails, setTransactionDetails] = useState(null);
  const [loading, setLoading] = useState(false);
  const [approveItems, setApproveItems] = useState([]);

  useEffect(() => {
    loadTransactions();
  }, []);

  const loadTransactions = async () => {
    try {
      const data = await workerLandingAPI.getTransactions();
      setTransactions(data);
    } catch (error) {
      console.error('Error loading transactions:', error);
    }
  };

  const handleViewDetails = async (transactionId) => {
    try {
      setLoading(true);
      const details = await workerStockAPI.getDetailedTransaction(transactionId);
      setTransactionDetails(details);
      setSelectedTransaction(transactionId);
      // Initialize approve items with received quantities
      if (details.productTransactionDtos) {
        setApproveItems(details.productTransactionDtos.map(item => ({
          productId: item.productId,
          quantityReceived: item.quantityReceived || item.expectedQuantity || 0,
        })));
      }
    } catch (error) {
      alert('Error loading transaction details: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleApproveItemChange = (productId, value) => {
    setApproveItems(prev => prev.map(item =>
      item.productId === productId
        ? { ...item, quantityReceived: parseInt(value) || 0 }
        : item
    ));
  };

  const handleApprove = async () => {
    if (!selectedTransaction) return;
    try {
      setLoading(true);
      await workerStockAPI.approveTransaction({
        transactionId: selectedTransaction,
        productTransactionDtos: approveItems.map(item => ({
          productId: item.productId,
          quantityReceived: item.quantityReceived,
        })),
      });
      alert('Transaction approved successfully!');
      setSelectedTransaction(null);
      setTransactionDetails(null);
      loadTransactions();
    } catch (error) {
      alert('Error approving transaction: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <div>
          <h1>Stock Transactions</h1>
          <a href="/worker/landing" style={{ marginRight: '20px' }}>← Back to Dashboard</a>
        </div>
        <button onClick={logout}>Logout</button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
        <div>
          <h2>Transactions</h2>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '2px solid #ccc' }}>
                <th style={{ padding: '10px', textAlign: 'left' }}>Transaction ID</th>
                <th style={{ padding: '10px', textAlign: 'left' }}>Type</th>
                <th style={{ padding: '10px', textAlign: 'left' }}>Werehouse</th>
                <th style={{ padding: '10px', textAlign: 'left' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map(transaction => (
                <tr key={transaction.transactionId} style={{ borderBottom: '1px solid #eee' }}>
                  <td style={{ padding: '10px' }}>{transaction.transactionId}</td>
                  <td style={{ padding: '10px' }}>{transaction.transactionType}</td>
                  <td style={{ padding: '10px' }}>{transaction.werehouseName}</td>
                  <td style={{ padding: '10px' }}>
                    <button onClick={() => handleViewDetails(transaction.transactionId)}>View Details</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div>
          {transactionDetails && (
            <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px' }}>
              <h2>Transaction Details</h2>
              <p><strong>Transaction ID:</strong> {transactionDetails.transactionId}</p>
              <p><strong>Type:</strong> {transactionDetails.transactionType}</p>
              <p><strong>Werehouse:</strong> {transactionDetails.werehouseName}</p>

              <h3 style={{ marginTop: '20px' }}>Products</h3>
              {transactionDetails.productTransactionDtos && transactionDetails.productTransactionDtos.map((item, index) => (
                <div key={index} style={{ marginBottom: '15px', padding: '10px', border: '1px solid #eee', borderRadius: '4px' }}>
                  <p><strong>Product ID:</strong> {item.productId}</p>
                  <p><strong>Expected Quantity:</strong> {item.expectedQuantity}</p>
                  <p><strong>Received Quantity:</strong> {item.quantityReceived || 0}</p>
                  <label>
                    Approve Quantity:
                    <input
                      type="number"
                      value={approveItems.find(ai => ai.productId === item.productId)?.quantityReceived || 0}
                      onChange={(e) => handleApproveItemChange(item.productId, e.target.value)}
                      style={{ marginLeft: '10px', padding: '5px' }}
                    />
                  </label>
                </div>
              ))}

              <button onClick={handleApprove} disabled={loading} style={{ marginTop: '20px', padding: '10px 20px' }}>
                {loading ? 'Approving...' : 'Approve Transaction'}
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Stock;

