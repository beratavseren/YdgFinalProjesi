import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signUpAPI, werehouseAPI } from '../services/api';

const SignUp = () => {
  const [formData, setFormData] = useState({
    nameSurname: '',
    telNo: '',
    email: '',
    password: '',
    werehouseId: '',
    userType: 'worker', // 'worker' or 'admin'
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const [werehouses, setWerehouses] = useState([]);
  const navigate = useNavigate();

  React.useEffect(() => {
    // Load werehouses for worker signup
    if (formData.userType === 'worker') {
      werehouseAPI.getWerehouses()
        .then(setWerehouses)
        .catch(err => console.error('Error loading werehouses:', err));
    }
  }, [formData.userType]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const submitData = {
        nameSurname: formData.nameSurname,
        telNo: formData.telNo,
        email: formData.email,
        password: formData.password,
      };

      if (formData.userType === 'worker') {
        submitData.werehouseId = parseInt(formData.werehouseId);
        const result = await signUpAPI.signUpWorker(submitData);
        if (result) {
          setSuccess('Worker account created successfully!');
          setTimeout(() => navigate('/login'), 2000);
        }
      } else {
        const result = await signUpAPI.signUpAdmin(submitData);
        if (result) {
          setSuccess('Admin account created successfully!');
          setTimeout(() => navigate('/login'), 2000);
        }
      }
    } catch (err) {
      setError(err.message || 'Sign up failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '50px auto', padding: '20px' }}>
      <h2>Sign Up</h2>
      {error && <div style={{ color: 'red', marginBottom: '10px' }}>{error}</div>}
      {success && <div style={{ color: 'green', marginBottom: '10px' }}>{success}</div>}
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '15px' }}>
          <label>
            User Type:
            <select
              name="userType"
              value={formData.userType}
              onChange={handleChange}
              style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            >
              <option value="worker">Worker</option>
              <option value="admin">Admin</option>
            </select>
          </label>
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label>
            Name Surname:
            <input
              type="text"
              name="nameSurname"
              value={formData.nameSurname}
              onChange={handleChange}
              required
              style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            />
          </label>
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label>
            Phone Number:
            <input
              type="tel"
              name="telNo"
              value={formData.telNo}
              onChange={handleChange}
              required
              style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            />
          </label>
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label>
            Email:
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            />
          </label>
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label>
            Password:
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            />
          </label>
        </div>
        {formData.userType === 'worker' && (
          <div style={{ marginBottom: '15px' }}>
            <label>
              Werehouse:
              <select
                name="werehouseId"
                value={formData.werehouseId}
                onChange={handleChange}
                required
                style={{ width: '100%', padding: '8px', marginTop: '5px' }}
              >
                <option value="">Select a werehouse</option>
                {werehouses.map(wh => (
                  <option key={wh.werehouseId} value={wh.werehouseId}>
                    {wh.werehouseName}
                  </option>
                ))}
              </select>
            </label>
          </div>
        )}
        <button type="submit" disabled={loading} style={{ width: '100%', padding: '10px' }}>
          {loading ? 'Signing up...' : 'Sign Up'}
        </button>
      </form>
      <div style={{ marginTop: '20px' }}>
        <a href="/login">Already have an account? Login</a>
      </div>
    </div>
  );
};

export default SignUp;

