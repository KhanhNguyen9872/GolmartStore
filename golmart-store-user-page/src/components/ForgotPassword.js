// src/components/ForgotPassword.js
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { forgotPassword } from '../api'; // Adjust path if needed

function ForgotPassword() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await forgotPassword(username, email);
      setMessage(response.message); // "Your new password is: 54U03TbgH4Be"
      setError('');
    } catch (err) {
      setError(err.message);
      setMessage('');
    }
  };

  return (
    <div className="flex justify-center">
      <div className="w-full max-w-md">
        <h1 className="text-5xl font-bold mb-8 text-center fictional-friend">Forgot Password</h1>
        <p className="text-center text-gray-700 mb-6">
          Enter your username and email address below, and we'll send you a link to reset your password.
        </p>
        {message && <p className="text-green-500 text-center mb-4">{message}</p>}
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
          <button type="submit" className="bg-green-200 text-black font-bold py-2 px-6 rounded-lg w-full">
            Send Reset Link
          </button>
        </form>
        <p className="text-center mt-4">
          Remembered your password?{' '}
          <Link to="/login" className="text-green-600 hover:underline">Log in</Link>
        </p>
      </div>
    </div>
  );
}

export default ForgotPassword;