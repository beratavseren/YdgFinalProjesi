import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

// Pages
import Login from './pages/Login';
import SignUp from './pages/SignUp';

// Admin Pages
import AdminDashboard from './pages/admin/Dashboard';
import Products from './pages/admin/Products';
import Categories from './pages/admin/Categories';
import Brands from './pages/admin/Brands';
import Werehouses from './pages/admin/Werehouses';
import Stock from './pages/admin/Stock';

// Worker Pages
import WorkerLanding from './pages/worker/Landing';
import WorkerProfile from './pages/worker/Profile';
import WorkerStock from './pages/worker/Stock';

// Home component that redirects based on role
const Home = () => {
  const { isAuthenticated, isAdmin, isWorker } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (isAdmin()) {
    return <Navigate to="/admin/dashboard" replace />;
  }

  if (isWorker()) {
    return <Navigate to="/worker/landing" replace />;
  }

  return <Navigate to="/login" replace />;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
          
          {/* Admin Routes */}
          <Route
            path="/admin/dashboard"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/products"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Products />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/categories"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Categories />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/brands"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Brands />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/werehouses"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Werehouses />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/stock"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Stock />
              </ProtectedRoute>
            }
          />

          {/* Worker Routes */}
          <Route
            path="/worker/landing"
            element={
              <ProtectedRoute requiredRole="WORKER">
                <WorkerLanding />
              </ProtectedRoute>
            }
          />
          <Route
            path="/worker/profile"
            element={
              <ProtectedRoute requiredRole="WORKER">
                <WorkerProfile />
              </ProtectedRoute>
            }
          />
          <Route
            path="/worker/stock"
            element={
              <ProtectedRoute requiredRole="WORKER">
                <WorkerStock />
              </ProtectedRoute>
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
