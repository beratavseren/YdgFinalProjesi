import React, { useState, useEffect } from 'react';
import { workerProfileAPI } from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';

const Profile = () => {
  const { logout } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showEditForm, setShowEditForm] = useState(false);
  const [showPasswordForm, setShowPasswordForm] = useState(false);
  const [editData, setEditData] = useState({ telNo: '', email: '' });
  const [passwordData, setPasswordData] = useState({ oldPassword: '', newPassword: '', confirmPassword: '' });
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      const data = await workerProfileAPI.getProfile();
      setProfile(data);
      setEditData({ telNo: data.telNo, email: data.email });
    } catch (error) {
      console.error('Error loading profile:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditData(prev => ({ ...prev, [name]: value }));
  };

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswordData(prev => ({ ...prev, [name]: value }));
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    try {
      await workerProfileAPI.updateProfile(editData);
      setMessage('Profile updated successfully!');
      setShowEditForm(false);
      loadProfile();
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error updating profile: ' + error.message);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setMessage('New passwords do not match!');
      return;
    }
    try {
      await workerProfileAPI.changePassword(passwordData);
      setMessage('Password changed successfully!');
      setShowPasswordForm(false);
      setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' });
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error changing password: ' + error.message);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h1>Profile</h1>
        <div>
          <a href="/worker/landing" style={{ marginRight: '20px' }}>← Back to Dashboard</a>
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      {message && (
        <div style={{ padding: '10px', marginBottom: '20px', backgroundColor: message.includes('Error') ? '#ffcccc' : '#ccffcc', borderRadius: '4px' }}>
          {message}
        </div>
      )}

      {profile && (
        <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
          <h2>Profile Information</h2>
          <p><strong>Name Surname:</strong> {profile.nameSurname}</p>
          <p><strong>Phone Number:</strong> {profile.telNo}</p>
          <p><strong>Email:</strong> {profile.email}</p>
          <p><strong>Werehouse:</strong> {profile.werehouseName}</p>
          <button onClick={() => setShowEditForm(true)} style={{ marginTop: '10px', marginRight: '10px' }}>Edit Profile</button>
          <button onClick={() => setShowPasswordForm(true)} style={{ marginTop: '10px' }}>Change Password</button>
        </div>
      )}

      {showEditForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
          <h2>Edit Profile</h2>
          <form onSubmit={handleUpdateProfile}>
            <div style={{ marginBottom: '15px' }}>
              <label>
                Phone Number:
                <input
                  type="tel"
                  name="telNo"
                  value={editData.telNo}
                  onChange={handleEditChange}
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
                  value={editData.email}
                  onChange={handleEditChange}
                  required
                  style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                />
              </label>
            </div>
            <button type="submit">Update</button>
            <button type="button" onClick={() => setShowEditForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}

      {showPasswordForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
          <h2>Change Password</h2>
          <form onSubmit={handleChangePassword}>
            <div style={{ marginBottom: '15px' }}>
              <label>
                Old Password:
                <input
                  type="password"
                  name="oldPassword"
                  value={passwordData.oldPassword}
                  onChange={handlePasswordChange}
                  required
                  style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                />
              </label>
            </div>
            <div style={{ marginBottom: '15px' }}>
              <label>
                New Password:
                <input
                  type="password"
                  name="newPassword"
                  value={passwordData.newPassword}
                  onChange={handlePasswordChange}
                  required
                  style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                />
              </label>
            </div>
            <div style={{ marginBottom: '15px' }}>
              <label>
                Confirm New Password:
                <input
                  type="password"
                  name="confirmPassword"
                  value={passwordData.confirmPassword}
                  onChange={handlePasswordChange}
                  required
                  style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                />
              </label>
            </div>
            <button type="submit">Change Password</button>
            <button type="button" onClick={() => setShowPasswordForm(false)} style={{ marginLeft: '10px' }}>Cancel</button>
          </form>
        </div>
      )}
    </div>
  );
};

export default Profile;

