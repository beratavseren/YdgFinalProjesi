import React, { useState, useEffect } from 'react';
import { productAPI, brandAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Products = () => {
  const { logout } = useAuth();
  const [products, setProducts] = useState([]);
  const [brands, setBrands] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);
  const [showEditForm, setShowEditForm] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [formData, setFormData] = useState({
    productName: '',
    barcodeNumber: '',
    brandId: '',
    comment: '',
    weigth: '',
    volume: '',
  });

  useEffect(() => {
    loadProducts();
    loadBrands();
  }, []);

  const loadProducts = async () => {
    try {
      const data = await productAPI.getProducts();
      setProducts(data);
    } catch (error) {
      console.error('Error loading products:', error);
    }
  };

  const loadBrands = async () => {
    try {
      const data = await brandAPI.getBrands();
      setBrands(data);
    } catch (error) {
      console.error('Error loading brands:', error);
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
      await productAPI.addProduct({
        productName: formData.productName,
        barcodeNumber: parseInt(formData.barcodeNumber),
        brandDto: { brandId: parseInt(formData.brandId) },
        comment: formData.comment,
        weigth: parseInt(formData.weigth),
        volume: parseInt(formData.volume),
      });
      loadProducts();
      setShowAddForm(false);
      setFormData({ productName: '', barcodeNumber: '', brandId: '', comment: '', weigth: '', volume: '' });
    } catch (error) {
      alert('Error adding product: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (product) => {
    setSelectedProduct(product);
    setFormData({
      productName: product.productName || '',
      comment: product.comment || '',
    });
    setShowEditForm(true);
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await productAPI.updateProduct({
        productId: selectedProduct.productId,
        productName: formData.productName,
        comment: formData.comment,
      });
      loadProducts();
      setShowEditForm(false);
      setSelectedProduct(null);
    } catch (error) {
      alert('Error updating product: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productAPI.deleteProduct(productId);
        loadProducts();
      } catch (error) {
        alert('Error deleting product: ' + error.message);
      }
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <div>
          <h1>Products</h1>
          <a href="/admin/dashboard" style={{ marginRight: '20px' }}>← Back to Dashboard</a>
        </div>
        <div>
          <button onClick={() => setShowAddForm(true)} style={{ marginRight: '10px' }}>Add Product</button>
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      {showAddForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Add Product</h2>
          <form onSubmit={handleAdd}>
            <div style={{ marginBottom: '10px' }}>
              <label>Product Name: <input type="text" name="productName" value={formData.productName} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Barcode Number: <input type="number" name="barcodeNumber" value={formData.barcodeNumber} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Brand: 
                <select name="brandId" value={formData.brandId} onChange={handleChange} required>
                  <option value="">Select brand</option>
                  {brands.map(brand => (
                    <option key={brand.brandId} value={brand.brandId}>{brand.brandName}</option>
                  ))}
                </select>
              </label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Comment: <textarea name="comment" value={formData.comment} onChange={handleChange} /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Weight: <input type="number" name="weigth" value={formData.weigth} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Volume: <input type="number" name="volume" value={formData.volume} onChange={handleChange} required /></label>
            </div>
            <button type="submit" disabled={loading}>Add</button>
            <button type="button" onClick={() => setShowAddForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      {showEditForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
          <h2>Edit Product</h2>
          <form onSubmit={handleUpdate}>
            <div style={{ marginBottom: '10px' }}>
              <label>Product Name: <input type="text" name="productName" value={formData.productName} onChange={handleChange} required /></label>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Comment: <textarea name="comment" value={formData.comment} onChange={handleChange} /></label>
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
            <th style={{ padding: '10px', textAlign: 'left' }}>Product Name</th>
            <th style={{ padding: '10px', textAlign: 'left' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map(product => (
            <tr key={product.productId} style={{ borderBottom: '1px solid #eee' }}>
              <td style={{ padding: '10px' }}>{product.productId}</td>
              <td style={{ padding: '10px' }}>{product.productName}</td>
              <td style={{ padding: '10px' }}>
                <button onClick={() => handleEdit(product)} style={{ marginRight: '5px' }}>Edit</button>
                <button onClick={() => handleDelete(product.productId)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Products;

