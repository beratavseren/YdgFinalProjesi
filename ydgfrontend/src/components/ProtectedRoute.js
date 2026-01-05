import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const ProtectedRoute = ({ children, requiredRole = null }) => {
  const { isAuthenticated, isAdmin, isWorker, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole === 'ADMIN' && !isAdmin()) {
    return <Navigate to="/" replace />;
  }

  if (requiredRole === 'WORKER' && !isWorker()) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;

