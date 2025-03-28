// src/components/Signup.js
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { register } from '../api'; // Adjust path if needed

function Signup() {
  const [username, setUsername] = useState('');
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    try {
      const userData = await register(username, password, fullName, email);
      // On success, redirect to login
      navigate('/login');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="flex justify-center">
      <div className="w-full max-w-md">
        <h1 className="text-5xl font-bold mb-8 text-center fictional-friend">Sign Up</h1>
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
            <label htmlFor="name" className="block text-sm font-bold mb-2">Full Name</label>
            <div className="relative">
              <input
                type="text"
                id="name"
                name="name"
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 pl-10 w-full"
                placeholder="Full Name"
                required
              />
              <i className="fas fa-user absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500"></i>
            </div>
          </div>
          <div>
            <label htmlFor="email" className="block text-sm font-bold mb-2">Email</label>
            <div className="relative">
              <input
                type="email"
                id="email"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 pl-10 w-full"
                placeholder="Email"
                required
              />
              <i className="fas fa-envelope absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500"></i>
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
          <div>
            <label htmlFor="confirm-password" className="block text-sm font-bold mb-2">Confirm Password</label>
            <div className="relative">
              <input
                type="password"
                id="confirm-password"
                name="confirm-password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 pl-10 w-full"
                placeholder="Confirm Password"
                required
              />
              <i className="fas fa-lock absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500"></i>
            </div>
          </div>
          <div className="flex items-center mb-4">
            <input type="checkbox" id="terms" name="terms" className="mr-2" required />
            <label htmlFor="terms" className="text-sm">
              I agree to the{' '}
              <Link to="/terms" className="text-green-600 hover:underline">
                Terms and Conditions
              </Link>
            </label>
          </div>
          <button type="submit" className="bg-green-200 text-black font-bold py-2 px-6 rounded-lg w-full">
            Sign Up
          </button>
        </form>
        <p className="text-center mt-4">
          Already have an account?{' '}
          <Link to="/login" className="text-green-600 hover:underline">Log in</Link>
        </p>
      </div>
    </div>
  );
}

export default Signup;