const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Helper function to get auth token from localStorage
const getAuthToken = () => {
  return localStorage.getItem('token');
};

// Helper function to make API requests
const apiRequest = async (endpoint, options = {}) => {
  const token = getAuthToken();
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || `HTTP error! status: ${response.status}`);
  }

  const contentType = response.headers.get('content-type');
  if (contentType && contentType.includes('application/json')) {
    return await response.json();
  } else if (contentType && contentType.includes('application/pdf')) {
    return await response.blob();
  } else {
    return await response.text();
  }
};

// Auth API
export const authAPI = {
  login: async (email, password) => {
    return apiRequest('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  },
  logout: async () => {
    return apiRequest('/auth/logout', {
      method: 'POST',
    });
  },
  hello: async () => {
    return apiRequest('/auth/hello');
  },
};

// SignUp API
export const signUpAPI = {
  signUpWorker: async (workerData) => {
    return apiRequest('/signUp/worker', {
      method: 'POST',
      body: JSON.stringify(workerData),
    });
  },
  signUpAdmin: async (adminData) => {
    return apiRequest('/signUp/admin', {
      method: 'POST',
      body: JSON.stringify(adminData),
    });
  },
};

// Product API
export const productAPI = {
  addProduct: async (productData) => {
    return apiRequest('/product/add', {
      method: 'POST',
      body: JSON.stringify(productData),
    });
  },
  updateProduct: async (productData) => {
    return apiRequest('/product/update', {
      method: 'PUT',
      body: JSON.stringify(productData),
    });
  },
  deleteProduct: async (productId) => {
    return apiRequest(`/product/delete/${productId}`, {
      method: 'DELETE',
    });
  },
  getProducts: async () => {
    return apiRequest('/product/getProducts');
  },
  listProduct: async () => {
    return apiRequest('/product/listProduct');
  },
  getProduct: async (productId) => {
    return apiRequest(`/product/getProduct/${productId}`);
  },
  getProductByBarcodeNumber: async (barcodeNumber) => {
    return apiRequest(`/product/getProductByBarcodeNumber/${barcodeNumber}`);
  },
};

// Category API
export const categoryAPI = {
  addCategory: async (categoryData) => {
    return apiRequest('/category/add', {
      method: 'POST',
      body: JSON.stringify(categoryData),
    });
  },
  updateCategory: async (categoryData) => {
    return apiRequest('/category/update', {
      method: 'PUT',
      body: JSON.stringify(categoryData),
    });
  },
  deleteCategory: async (categoryId) => {
    return apiRequest(`/category/delete/${categoryId}`, {
      method: 'DELETE',
    });
  },
  getCategory: async (categoryId) => {
    return apiRequest(`/category/getCategory/${categoryId}`);
  },
  getCategories: async () => {
    return apiRequest('/category/getCategories');
  },
};

// Brand API
export const brandAPI = {
  addBrand: async (brandData) => {
    return apiRequest('/brand/add', {
      method: 'POST',
      body: JSON.stringify(brandData),
    });
  },
  updateBrand: async (brandData) => {
    return apiRequest('/brand/update', {
      method: 'PUT',
      body: JSON.stringify(brandData),
    });
  },
  deleteBrand: async (brandId) => {
    return apiRequest(`/brand/delete/${brandId}`, {
      method: 'DELETE',
    });
  },
  getBrand: async (brandId) => {
    return apiRequest(`/brand/getBrand/${brandId}`);
  },
  getBrands: async () => {
    return apiRequest('/brand/getBrands');
  },
};

// Werehouse API
export const werehouseAPI = {
  addWerehouse: async (werehouseData) => {
    return apiRequest('/werehouse/add', {
      method: 'POST',
      body: JSON.stringify(werehouseData),
    });
  },
  updateWerehouse: async (werehouseData) => {
    return apiRequest('/werehouse/update', {
      method: 'PUT',
      body: JSON.stringify(werehouseData),
    });
  },
  deleteWerehouse: async (werehouseId) => {
    return apiRequest(`/werehouse/delete/${werehouseId}`, {
      method: 'DELETE',
    });
  },
  getWerehouse: async (werehouseId) => {
    return apiRequest(`/werehouse/getWerehouse/${werehouseId}`);
  },
  getWerehouses: async () => {
    return apiRequest('/werehouse/getWerehouses');
  },
  getWerehousesWithWeigth: async () => {
    return apiRequest('/werehouse/getWerehousesWithWeigth');
  },
};

// Stock API (Admin)
export const stockAPI = {
  inProduct: async (transactionData) => {
    return apiRequest('/stock/in', {
      method: 'POST',
      body: JSON.stringify(transactionData),
    });
  },
  outProduct: async (transactionData) => {
    return apiRequest('/stock/out', {
      method: 'POST',
      body: JSON.stringify(transactionData),
    });
  },
  betweenWerehouses: async (transactionData) => {
    return apiRequest('/stock/betweenWerehouses', {
      method: 'POST',
      body: JSON.stringify(transactionData),
    });
  },
  getDetailedTransaction: async (transactionId) => {
    return apiRequest(`/stock/getDetailedTransaction/${transactionId}`);
  },
  getTransactionsWithFilter: async (filters = {}) => {
    const params = new URLSearchParams();
    if (filters.transactionType) params.append('transactionType', filters.transactionType);
    if (filters.werehouseId) params.append('werehouseId', filters.werehouseId);
    if (filters.transactionStuation !== undefined) params.append('transactionStuation', filters.transactionStuation);
    
    const queryString = params.toString();
    return apiRequest(`/stock/getTransactionsWithFilter${queryString ? '?' + queryString : ''}`);
  },
  getReceipt: async (transactionId) => {
    if (transactionId) {
      return apiRequest(`/stock/getReceipt/${transactionId}`);
    }
    return apiRequest('/stock/getReceipt');
  },
  searchByTransactionId: async (transactionId) => {
    return apiRequest(`/stock/searchByTransactionId?transactionId=${transactionId}`);
  },
  setCriticalStockLevel: async (data) => {
    return apiRequest('/stock/setCriticalStockLevel', {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },
};

// Werehouse Worker Landing API
export const workerLandingAPI = {
  getWerehouseWorkerLandingInfo: async () => {
    return apiRequest('/werehouseWorkerLanding/getWerehouseWorkerLandingInfo');
  },
  getProducts: async () => {
    return apiRequest('/werehouseWorkerLanding/getProducts');
  },
  getCriticalStockCount: async () => {
    return apiRequest('/werehouseWorkerLanding/getCriticalStockCount');
  },
  getTransactions: async () => {
    return apiRequest('/werehouseWorkerLanding/getTransactions');
  },
};

// Werehouse Worker Stock API
export const workerStockAPI = {
  getDetailedTransaction: async (transactionId) => {
    return apiRequest(`/werehouseWorkerStock/getDetailedTransaction/${transactionId}`);
  },
  approveTransaction: async (approveData) => {
    return apiRequest('/werehouseWorkerStock/approveTransaction', {
      method: 'PUT',
      body: JSON.stringify(approveData),
    });
  },
};

// Werehouse Worker Profile API
export const workerProfileAPI = {
  getProfile: async () => {
    return apiRequest('/werehouseWorkerProfile/getProfile');
  },
  updateProfile: async (profileData) => {
    return apiRequest('/werehouseWorkerProfile/updateProfile', {
      method: 'PUT',
      body: JSON.stringify(profileData),
    });
  },
  changePassword: async (passwordData) => {
    return apiRequest('/werehouseWorkerProfile/changePassword', {
      method: 'PUT',
      body: JSON.stringify(passwordData),
    });
  },
};

