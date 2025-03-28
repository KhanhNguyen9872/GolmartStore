// src/components/Login.js
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login, getUserInfo } from '../api'; // Adjust path if needed

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const loginData = await login(username, password);
      const { token } = loginData;

      // Save token to localStorage
      localStorage.setItem('token', token);

      // Fetch user info
      const userData = await getUserInfo(token);
      const { fullName } = userData;

      // Save fullName to localStorage
      localStorage.setItem('fullName', fullName);

      // Redirect to profile
      navigate('/profile');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="flex justify-center">
      <div className="w-full max-w-md">
        <h1 className="text-5xl font-bold mb-8 text-center fictional-friend">Log In</h1>
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="username" className="block text-sm font-bold mb-2">Username</label>
            <div className="relative">
              <input
                type="text"
                id="username"
                name="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 pl-10 w-full"
                placeholder="Username"
                required
              />
              <i className="fas fa-user absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500"></i>
            </div>
          </div>
          <div>
            <label htmlFor="password" className="block text-sm font-bold mb-2">Password</label>
            <div className="relative">
              <input
                type="password"
                id="password"
                name="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 pl-10 w-full"
                placeholder="Password"
                required
              />
              <i className="fas fa-lock absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500"></i>
            </div>
          </div>
          <div className="flex justify-between items-center mb-4">
            <div className="flex items-center">
              <input type="checkbox" id="remember" name="remember" className="mr-2" />
              <label htmlFor="remember" className="text-sm">Remember me</label>
            </div>
            <Link to="/forgot-password" className="text-sm text-green-600 hover:underline">
              Forgot password?
            </Link>
          </div>
          <button type="submit" className="bg-green-200 text-black font-bold py-2 px-6 rounded-lg w-full">
            Log In
          </button>
        </form>
        <p className="text-center mt-4">
          Don't have an account?{' '}
          <Link to="/signup" className="text-green-600 hover:underline">Sign up</Link>
        </p>
      </div>
    </div>
  );
}

export default Login;