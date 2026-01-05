import React, { useState, useEffect } from 'react';
import { werehouseAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Werehouses = () => {
  const { logout } = useAuth();
  const [werehouses, setWerehouses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);
  const [showEditForm, setShowEditForm] = useState(false);
  const [selectedWerehouse, setSelectedWerehouse] = useState(null);
  const [formData, setFormData] = useState({
    werehouseName: '',
    werehouseLocation: '',
    weigthLimit: '',
    volumeLimit: '',
  });

  useEffect(() => {
    loadWerehouses();
  }, []);

  const loadWerehouses = async () => {
    try {
      const data = await werehouseAPI.getWerehouses();
      setWerehouses(data);
    } catch (error) {
      console.error('Error loading werehouses:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await werehouseAPI.addWerehouse({
        werehouseName: formData.werehouseName,
        werehouseLocation: formData.werehouseLocation,
        weigthLimit: formData.weigthLimit ? parseFloat(formData.weigthLimit) : null,
        volumeLimit: formData.volumeLimit ? parseFloat(formData.volumeLimit) : null,
      });
      loadWerehouses();
      setShowAddForm(false);
      setFormData({ werehouseName: '', werehouseLocation: '', weigthLimit: '', volumeLimit: '' });
    } catch (error) {
      alert('Error adding werehouse: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (werehouse) => {
    setSelectedWerehouse(werehouse);
    setFormData({
      werehouseName: werehouse.werehouseName || '',
      werehouseLocation: werehouse.werehouseLocation || '',
      weigthLimit: werehouse.weigthLimit || '',
      volumeLimit: werehouse.volumeLimit || '',
    });
    setShowEditForm(true);
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await werehouseAPI.updateWerehouse({
        werehouseId: selectedWerehouse.werehouseId,
        werehouseName: formData.werehouseName,
        weigthLimit: formData.weigthLimit ? parseFloat(formData.weigthLimit) : null,
        volumeLimit: formData.volumeLimit ? parseFloat(formData.volumeLimit) : null,
      });
      loadWerehouses();
      setShowEditForm(false);
      setSelectedWerehouse(null);
    } catch (error) {
      alert('Error updating werehouse: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (werehouseId) => {
    if (window.confirm('Are you sure you want to delete this werehouse?')) {
      try {
        await werehouseAPI.deleteWerehouse(werehouseId);
        loadWerehouses();
      } catch (error) {
        alert('Error deleting werehouse: ' + error.message);
      }
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <div>
          <h1>Werehouses</h1>
          <a href="/admin/dashboard" style={{ marginRight: '20px' }}>← Back to Dashboard</a>
        </div>
        <div>
          <button onClick={() => setShowAddForm(true)} style={{ marginRight: '10px' }}>Add Werehouse</button>
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      {showAddForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Add Werehouse</h2>
          <form onSubmit={handleAdd}>
            <div style={{ marginBottom: '10px' }}>
              <label>Werehouse Name: <input type="text" name="werehouseName" value={formData.werehouseName} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Location: <input type="text" name="werehouseLocation" value={formData.werehouseLocation} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Weight Limit: <input type="number" step="0.01" name="weigthLimit" value={formData.weigthLimit} onChange={handleChange} /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Volume Limit: <input type="number" step="0.01" name="volumeLimit" value={formData.volumeLimit} onChange={handleChange} /></label>
            </div>
            <button type="submit" disabled={loading}>Add</button>
            <button type="button" onClick={() => setShowAddForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      {showEditForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Edit Werehouse</h2>
          <form onSubmit={handleUpdate}>
            <div style={{ marginBottom: '10px' }}>
              <label>Werehouse Name: <input type="text" name="werehouseName" value={formData.werehouseName} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Weight Limit: <input type="number" step="0.01" name="weigthLimit" value={formData.weigthLimit} onChange={handleChange} /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Volume Limit: <input type="number" step="0.01" name="volumeLimit" value={formData.volumeLimit} onChange={handleChange} /></label>
            </div>
            <button type="submit" disabled={loading}>Update</button>
            <button type="button" onClick={() => setShowEditForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #ccc' }}>
            <th style={{ padding: '10px', textAlign: 'left' }}>ID</th>
            <th style={{ padding: '10px', textAlign: 'left' }}>Werehouse Name</th>
            <th style={{ padding: '10px', textAlign: 'left' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {werehouses.map(werehouse => (
            <tr key={werehouse.werehouseId} style={{ borderBottom: '1px solid #eee' }}>
              <td style={{ padding: '10px' }}>{werehouse.werehouseId}</td>
              <td style={{ padding: '10px' }}>{werehouse.werehouseName}</td>
              <td style={{ padding: '10px' }}>
                <button onClick={() => handleEdit(werehouse)} style={{ marginRight: '5px' }}>Edit</button>
                <button onClick={() => handleDelete(werehouse.werehouseId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Werehouses;

