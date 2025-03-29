// src/api.js
import axios from 'axios';

const API_URL = '';

// Login API call
export const login = async (username, password) => {
  try {
    const response = await axios.post(`${API_URL}/api/login`, {
      username,
      password,
    }, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return response.data; // { token }
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Login failed');
  }
};

// Get user info API call
export const getUserInfo = async (token) => {
  try {
    const response = await axios.get(`${API_URL}/api/user`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
    return response.data; // { username, fullName, email }
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Failed to fetch user info');
  }
};

// Register API call
export const register = async (username, password, fullName, email) => {
  try {
    const response = await axios.post(`${API_URL}/api/register`, {
      username,
      password,
      fullName,
      email,
    }, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return response.data; // { username, fullName, email }
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Registration failed');
  }
};

// Forgot Password API call
export const forgotPassword = async (username, email) => {
  try {
    const response = await axios.post(`${API_URL}/api/forgot`, {
      username,
      email,
    }, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return response.data; // { "message": "Your new password is: 54U03TbgH4Be" }
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Forgot password request failed');
  }
};

// Get Products API call
export const getProducts = async () => {
    try {
      const response = await axios.get(`${API_URL}/api/products`);
      
      // Check if response.data exists and is an array
      if (!response.data || !Array.isArray(response.data)) {
        throw new Error('Expected an array of products');
      }
  
      // Map over the array to create a new array with updated image fields
      const products = response.data.map(product => ({
        ...product, // Spread all existing fields of the product
        image: API_URL + '/api/' + product.image // Prepend API_URL to the image field
      }));
  
      return products; // Return the modified array
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch products');
    }
  };

  // Function to fetch a single product by ID
export const getProductById = async (id) => {
    try {
      const response = await axios.get(`${API_URL}/api/products/${id}`);
      if (response.data) {
        response.data.image = API_URL + '/api/' + response.data.image; // Prepend API_URL to the image field
      }
      return response.data; // Returns the product object
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch product');
    }
  };

  export const getSearchProduct = async (query) => {
    try {
      const response = await axios.get(`${API_URL}/api/products/search`, {
        params: { q: query },
      });

       // Map over the array to create a new array with updated image fields
       const products = response.data.map(product => ({
        ...product, // Spread all existing fields of the product
        image: API_URL + '/api/' + product.image // Prepend API_URL to the image field
      }));
      return products; // Expecting an array of products from the API
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch search results');
    }
  };

  // Get Cart Info API call
export const getCartInfo = async (token) => {
    try {
      const response = await axios.get(`${API_URL}/api/cart`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      return response.data; // Array of cart items
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch cart info');
    }
  };

  // Add to Cart API call
export const addToCart = async (token, productId, quantity) => {
    try {
      const response = await axios.post(
        `${API_URL}/api/cart`,
        {
          productId,
          quantity,
        },
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );
      return response.data; // Assuming the API returns a success message or cart data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to add to cart');
    }
  };

  // Remove product from cart
export const removeFromCart = async (token, productId) => {
    try {
      const response = await axios.delete(`${API_URL}/api/cart/${productId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      return response.data; // { message: "Product removed from cart" }
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to remove product from cart');
    }
  };
  
  // Update product quantity in cart
  export const updateCartQuantity = async (token, productId, quantity) => {
    try {
      const response = await axios.put(
        `${API_URL}/api/cart/${productId}`,
        { quantity },
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );
      return response.data; // Assuming the API returns updated cart data or a success message
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to update cart quantity');
    }
  };