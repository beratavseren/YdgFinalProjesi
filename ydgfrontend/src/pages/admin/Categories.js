import React, { useState, useEffect } from 'react';
import { categoryAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Categories = () => {
  const { logout } = useAuth();
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);
  const [showEditForm, setShowEditForm] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [formData, setFormData] = useState({ categoryName: '' });

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      const data = await categoryAPI.getCategories();
      setCategories(data);
    } catch (error) {
      console.error('Error loading categories:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({ categoryName: e.target.value });
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await categoryAPI.addCategory(formData);
      loadCategories();
      setShowAddForm(false);
      setFormData({ categoryName: '' });
    } catch (error) {
      alert('Error adding category: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (category) => {
    setSelectedCategory(category);
    setFormData({ categoryName: category.categoryName });
    setShowEditForm(true);
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await categoryAPI.updateCategory({
        categoryId: selectedCategory.categoryId,
        categoryName: formData.categoryName,
      });
      loadCategories();
      setShowEditForm(false);
      setSelectedCategory(null);
    } catch (error) {
      alert('Error updating category: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (categoryId) => {
    if (window.confirm('Are you sure you want to delete this category?')) {
      try {
        await categoryAPI.deleteCategory(categoryId);
        loadCategories();
      } catch (error) {
        alert('Error deleting category: ' + error.message);
      }
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <div>
          <h1>Categories</h1>
          <a href="/admin/dashboard" style={{ marginRight: '20px' }}>← Back to Dashboard</a>
        </div>
        <div>
          <button onClick={() => setShowAddForm(true)} style={{ marginRight: '10px' }}>Add Category</button>
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      {showAddForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Add Category</h2>
          <form onSubmit={handleAdd}>
            <div style={{ marginBottom: '10px' }}>
              <label>Category Name: <input type="text" value={formData.categoryName} onChange={handleChange} required /></label>
            </div>
            <button type="submit" disabled={loading}>Add</button>
            <button type="button" onClick={() => setShowAddForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      {showEditForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Edit Category</h2>
          <form onSubmit={handleUpdate}>
            <div style={{ marginBottom: '10px' }}>
              <label>Category Name: <input type="text" value={formData.categoryName} onChange={handleChange} required /></label>
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
            <th style={{ padding: '10px', textAlign: 'left' }}>Category Name</th>
            <th style={{ padding: '10px', textAlign: 'left' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {categories.map(category => (
            <tr key={category.categoryId} style={{ borderBottom: '1px solid #eee' }}>
              <td style={{ padding: '10px' }}>{category.categoryId}</td>
              <td style={{ padding: '10px' }}>{category.categoryName}</td>
              <td style={{ padding: '10px' }}>
                <button onClick={() => handleEdit(category)} style={{ marginRight: '5px' }}>Edit</button>
                <button onClick={() => handleDelete(category.categoryId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Categories;

