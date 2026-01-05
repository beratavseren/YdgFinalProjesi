import React, { useState, useEffect } from 'react';
import { brandAPI, categoryAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Brands = () => {
  const { logout } = useAuth();
  const [brands, setBrands] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);
  const [showEditForm, setShowEditForm] = useState(false);
  const [selectedBrand, setSelectedBrand] = useState(null);
  const [formData, setFormData] = useState({
    brandName: '',
    selectedCategories: [],
  });

  useEffect(() => {
    loadBrands();
    loadCategories();
  }, []);

  const loadBrands = async () => {
    try {
      const data = await brandAPI.getBrands();
      setBrands(data);
    } catch (error) {
      console.error('Error loading brands:', error);
    }
  };

  const loadCategories = async () => {
    try {
      const data = await categoryAPI.getCategories();
      setCategories(data);
    } catch (error) {
      console.error('Error loading categories:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, brandName: e.target.value });
  };

  const handleCategoryToggle = (categoryId) => {
    setFormData(prev => {
      const selected = prev.selectedCategories.includes(categoryId)
        ? prev.selectedCategories.filter(id => id !== categoryId)
        : [...prev.selectedCategories, categoryId];
      return { ...prev, selectedCategories: selected };
    });
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await brandAPI.addBrand({
        brandName: formData.brandName,
        categoryDtos: formData.selectedCategories.map(id => ({ categoryId: id })),
      });
      loadBrands();
      setShowAddForm(false);
      setFormData({ brandName: '', selectedCategories: [] });
    } catch (error) {
      alert('Error adding brand: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (brand) => {
    setSelectedBrand(brand);
    setFormData({
      brandName: brand.brandName,
      selectedCategories: brand.categoryDtos?.map(c => c.categoryId) || [],
    });
    setShowEditForm(true);
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await brandAPI.updateBrand({
        brandId: selectedBrand.brandId,
        brandName: formData.brandName,
        categoryDtos: formData.selectedCategories.map(id => ({ categoryId: id })),
      });
      loadBrands();
      setShowEditForm(false);
      setSelectedBrand(null);
    } catch (error) {
      alert('Error updating brand: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (brandId) => {
    if (window.confirm('Are you sure you want to delete this brand?')) {
      try {
        await brandAPI.deleteBrand(brandId);
        loadBrands();
      } catch (error) {
        alert('Error deleting brand: ' + error.message);
      }
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <div>
          <h1>Brands</h1>
          <a href="/admin/dashboard" style={{ marginRight: '20px' }}>← Back to Dashboard</a>
        </div>
        <div>
          <button onClick={() => setShowAddForm(true)} style={{ marginRight: '10px' }}>Add Brand</button>
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      {showAddForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Add Brand</h2>
          <form onSubmit={handleAdd}>
            <div style={{ marginBottom: '10px' }}>
              <label>Brand Name: <input type="text" value={formData.brandName} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Categories:</label>
              {categories.map(category => (
                <div key={category.categoryId}>
                  <input
                    type="checkbox"
                    checked={formData.selectedCategories.includes(category.categoryId)}
                    onChange={() => handleCategoryToggle(category.categoryId)}
                  />
                  {category.categoryName}
                </div>
              ))}
            </div>
            <button type="submit" disabled={loading}>Add</button>
            <button type="button" onClick={() => setShowAddForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      {showEditForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Edit Brand</h2>
          <form onSubmit={handleUpdate}>
            <div style={{ marginBottom: '10px' }}>
              <label>Brand Name: <input type="text" value={formData.brandName} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Categories:</label>
              {categories.map(category => (
                <div key={category.categoryId}>
                  <input
                    type="checkbox"
                    checked={formData.selectedCategories.includes(category.categoryId)}
                    onChange={() => handleCategoryToggle(category.categoryId)}
                  />
                  {category.categoryName}
                </div>
              ))}
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
            <th style={{ padding: '10px', textAlign: 'left' }}>Brand Name</th>
            <th style={{ padding: '10px', textAlign: 'left' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {brands.map(brand => (
            <tr key={brand.brandId} style={{ borderBottom: '1px solid #eee' }}>
              <td style={{ padding: '10px' }}>{brand.brandId}</td>
              <td style={{ padding: '10px' }}>{brand.brandName}</td>
              <td style={{ padding: '10px' }}>
                <button onClick={() => handleEdit(brand)} style={{ marginRight: '5px' }}>Edit</button>
                <button onClick={() => handleDelete(brand.brandId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Brands;

