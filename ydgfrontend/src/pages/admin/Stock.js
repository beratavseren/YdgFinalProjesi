import React, { useState, useEffect } from 'react';
import { stockAPI, werehouseAPI, productAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Stock = () => {
  const { logout } = useAuth();
  const [transactions, setTransactions] = useState([]);
  const [werehouses, setWerehouses] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showInForm, setShowInForm] = useState(false);
  const [showOutForm, setShowOutForm] = useState(false);
  const [showBetweenForm, setShowBetweenForm] = useState(false);
  const [transactionItems, setTransactionItems] = useState([{ productId: '', expectedQuantity: '' }]);
  const [formData, setFormData] = useState({
    werehouseId: '',
    fromWerehouseId: '',
    toWerehouseId: '',
  });
  const [filters, setFilters] = useState({
    transactionType: '',
    werehouseId: '',
    transactionStuation: '',
  });

  useEffect(() => {
    loadTransactions();
    loadWerehouses();
    loadProducts();
  }, []);

  const loadTransactions = async () => {
    try {
      const data = await stockAPI.getTransactionsWithFilter(filters);
      setTransactions(data);
    } catch (error) {
      console.error('Error loading transactions:', error);
    }
  };

  const loadWerehouses = async () => {
    try {
      const data = await werehouseAPI.getWerehouses();
      setWerehouses(data);
    } catch (error) {
      console.error('Error loading werehouses:', error);
    }
  };

  const loadProducts = async () => {
    try {
      const data = await productAPI.getProducts();
      setProducts(data);
    } catch (error) {
      console.error('Error loading products:', error);
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handleApplyFilters = () => {
    loadTransactions();
  };

  const handleAddItem = () => {
    setTransactionItems([...transactionItems, { productId: '', expectedQuantity: '' }]);
  };

  const handleItemChange = (index, field, value) => {
    const newItems = [...transactionItems];
    newItems[index][field] = value;
    setTransactionItems(newItems);
  };

  const handleRemoveItem = (index) => {
    setTransactionItems(transactionItems.filter((_, i) => i !== index));
  };

  const handleIn = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await stockAPI.inProduct({
        werehouseId: parseInt(formData.werehouseId),
        productTransactionDtos: transactionItems.map(item => ({
          productId: parseInt(item.productId),
          expectedQuantity: parseInt(item.expectedQuantity),
        })),
      });
      setShowInForm(false);
      setTransactionItems([{ productId: '', expectedQuantity: '' }]);
      setFormData({ werehouseId: '' });
      loadTransactions();
    } catch (error) {
      alert('Error adding stock: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleOut = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await stockAPI.outProduct({
        werehouseId: parseInt(formData.werehouseId),
        productTransactionDtos: transactionItems.map(item => ({
          productId: parseInt(item.productId),
          expectedQuantity: parseInt(item.expectedQuantity),
        })),
      });
      setShowOutForm(false);
      setTransactionItems([{ productId: '', expectedQuantity: '' }]);
      setFormData({ werehouseId: '' });
      loadTransactions();
    } catch (error) {
      alert('Error removing stock: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleBetween = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await stockAPI.betweenWerehouses({
        fromWerehouseId: parseInt(formData.fromWerehouseId),
        toWerehouseId: parseInt(formData.toWerehouseId),
        productTransactionDtos: transactionItems.map(item => ({
          productId: parseInt(item.productId),
          expectedQuantity: parseInt(item.expectedQuantity),
        })),
      });
      setShowBetweenForm(false);
      setTransactionItems([{ productId: '', expectedQuantity: '' }]);
      setFormData({ fromWerehouseId: '', toWerehouseId: '' });
      loadTransactions();
    } catch (error) {
      alert('Error transferring stock: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <div>
          <h1>Stock Management</h1>
          <a href="/admin/dashboard" style={{ marginRight: '20px' }}>← Back to Dashboard</a>
        </div>
        <button onClick={logout}>Logout</button>
      </div>

      <div style={{ marginBottom: '20px', display: 'flex', gap: '10px' }}>
        <button onClick={() => setShowInForm(true)}>Stock In</button>
        <button onClick={() => setShowOutForm(true)}>Stock Out</button>
        <button onClick={() => setShowBetweenForm(true)}>Transfer Between Werehouses</button>
      </div>

      <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
        <h3>Filters</h3>
        <div style={{ display: 'flex', gap: '10px', marginBottom: '10px' }}>
          <select name="transactionType" value={filters.transactionType} onChange={handleFilterChange}>
            <option value="">All Types</option>
            <option value="IN">IN</option>
            <option value="OUT">OUT</option>
            <option value="BETWEEN">BETWEEN</option>
          </select>
          <select name="werehouseId" value={filters.werehouseId} onChange={handleFilterChange}>
            <option value="">All Werehouses</option>
            {werehouses.map(wh => (
              <option key={wh.werehouseId} value={wh.werehouseId}>{wh.werehouseName}</option>
            ))}
          </select>
          <button onClick={handleApplyFilters}>Apply Filters</button>
        </div>
      </div>

      {showInForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Stock In</h2>
          <form onSubmit={handleIn}>
            <div style={{ marginBottom: '10px' }}>
              <label>Werehouse: 
                <select value={formData.werehouseId} onChange={(e) => setFormData({ ...formData, werehouseId: e.target.value })} required>
                  <option value="">Select werehouse</option>
                  {werehouses.map(wh => (
                    <option key={wh.werehouseId} value={wh.werehouseId}>{wh.werehouseName}</option>
                  ))}
                </select>
              </label>
            </div>
            {transactionItems.map((item, index) => (
              <div key={index} style={{ marginBottom: '10px', display: 'flex', gap: '10px' }}>
                <select
                  value={item.productId}
                  onChange={(e) => handleItemChange(index, 'productId', e.target.value)}
                  required
                >
                  <option value="">Select product</option>
                  {products.map(p => (
                    <option key={p.productId} value={p.productId}>{p.productName}</option>
                  ))}
                </select>
                <input
                  type="number"
                  placeholder="Quantity"
                  value={item.expectedQuantity}
                  onChange={(e) => handleItemChange(index, 'expectedQuantity', e.target.value)}
                  required
                />
                {transactionItems.length > 1 && (
                  <button type="button" onClick={() => handleRemoveItem(index)}>Remove</button>
                )}
              </div>
            ))}
            <button type="button" onClick={handleAddItem} style={{ marginBottom: '10px' }}>Add Item</button>
            <br />
            <button type="submit" disabled={loading}>Submit</button>
            <button type="button" onClick={() => setShowInForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      {showOutForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Stock Out</h2>
          <form onSubmit={handleOut}>
            <div style={{ marginBottom: '10px' }}>
              <label>Werehouse: 
                <select value={formData.werehouseId} onChange={(e) => setFormData({ ...formData, werehouseId: e.target.value })} required>
                  <option value="">Select werehouse</option>
                  {werehouses.map(wh => (
                    <option key={wh.werehouseId} value={wh.werehouseId}>{wh.werehouseName}</option>
                  ))}
                </select>
              </label>
            </div>
            {transactionItems.map((item, index) => (
              <div key={index} style={{ marginBottom: '10px', display: 'flex', gap: '10px' }}>
                <select
                  value={item.productId}
                  onChange={(e) => handleItemChange(index, 'productId', e.target.value)}
                  required
                >
                  <option value="">Select product</option>
                  {products.map(p => (
                    <option key={p.productId} value={p.productId}>{p.productName}</option>
                  ))}
                </select>
                <input
                  type="number"
                  placeholder="Quantity"
                  value={item.expectedQuantity}
                  onChange={(e) => handleItemChange(index, 'expectedQuantity', e.target.value)}
                  required
                />
                {transactionItems.length > 1 && (
                  <button type="button" onClick={() => handleRemoveItem(index)}>Remove</button>
                )}
              </div>
            ))}
            <button type="button" onClick={handleAddItem} style={{ marginBottom: '10px' }}>Add Item</button>
            <br />
            <button type="submit" disabled={loading}>Submit</button>
            <button type="button" onClick={() => setShowOutForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      {showBetweenForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Transfer Between Werehouses</h2>
          <form onSubmit={handleBetween}>
            <div style={{ marginBottom: '10px' }}>
              <label>From Werehouse: 
                <select value={formData.fromWerehouseId} onChange={(e) => setFormData({ ...formData, fromWerehouseId: e.target.value })} required>
                  <option value="">Select werehouse</option>
                  {werehouses.map(wh => (
                    <option key={wh.werehouseId} value={wh.werehouseId}>{wh.werehouseName}</option>
                  ))}
                </select>
              </label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>To Werehouse: 
                <select value={formData.toWerehouseId} onChange={(e) => setFormData({ ...formData, toWerehouseId: e.target.value })} required>
                  <option value="">Select werehouse</option>
                  {werehouses.map(wh => (
                    <option key={wh.werehouseId} value={wh.werehouseId}>{wh.werehouseName}</option>
                  ))}
                </select>
              </label>
            </div>
            {transactionItems.map((item, index) => (
              <div key={index} style={{ marginBottom: '10px', display: 'flex', gap: '10px' }}>
                <select
                  value={item.productId}
                  onChange={(e) => handleItemChange(index, 'productId', e.target.value)}
                  required
                >
                  <option value="">Select product</option>
                  {products.map(p => (
                    <option key={p.productId} value={p.productId}>{p.productName}</option>
                  ))}
                </select>
                <input
                  type="number"
                  placeholder="Quantity"
                  value={item.expectedQuantity}
                  onChange={(e) => handleItemChange(index, 'expectedQuantity', e.target.value)}
                  required
                />
                {transactionItems.length > 1 && (
                  <button type="button" onClick={() => handleRemoveItem(index)}>Remove</button>
                )}
              </div>
            ))}
            <button type="button" onClick={handleAddItem} style={{ marginBottom: '10px' }}>Add Item</button>
            <br />
            <button type="submit" disabled={loading}>Submit</button>
            <button type="button" onClick={() => setShowBetweenForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ padding: '10px', textAlign: 'left' }}>Transaction ID</th>
            <th style={{ padding: '10px', textAlign: 'left' }}>Type</th>
            <th style={{ padding: '10px', textAlign: 'left' }}>Werehouse</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map(transaction => (
            <tr key={transaction.transactionId} style={{ borderBottom: '1px solid #eee' }}>
              <td style={{ padding: '10px' }}>{transaction.transactionId}</td>
              <td style={{ padding: '10px' }}>{transaction.transactionType}</td>
              <td style={{ padding: '10px' }}>{transaction.werehouseName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Stock;

