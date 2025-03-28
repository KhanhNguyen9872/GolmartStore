// src/components/Header.js
import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getUserInfo } from '../api'; // Adjust the path if api.js is elsewhere

function Header() {
  const [searchQuery, setSearchQuery] = useState('');
  const [fullName, setFullName] = useState('');
  const navigate = useNavigate();

  // Stabilize handleLogout with useCallback
  const handleLogout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('fullName'); // Optional, since we're not relying on it
    setFullName('');
    navigate('/');
  }, [navigate]);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      getUserInfo(token)
        .then((data) => {
          setFullName(data.fullName); // Assuming the API returns { fullName: "..." }
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            // If token is invalid (401), log the user out
            handleLogout();
          } else {
            // Log other errors for debugging, but don't disrupt the UI
            console.error('Failed to fetch user info:', error);
          }
        });
    }
  }, [handleLogout]); // Dependency on handleLogout since it's called in catch

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
    }
  };

  const isLoggedIn = !!localStorage.getItem('token'); // Re-evaluated on every render

  return (
    <div className="flex justify-end items-center mb-8">
      <form onSubmit={handleSearch} className="relative">
        <input
          name="q"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="bg-[#f8d4ac] border border-gray-300 rounded-full py-2 px-4 pl-10"
          style={{ width: '320px' }}
          placeholder="Search products..."
          type="text"
          aria-label="Search"
        />
        <button
          type="submit"
          className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 bg-transparent border-none p-0"
          aria-label="Search"
        >
          <i className="fas fa-search"></i>
        </button>
      </form>
      {!isLoggedIn ? (
        <Link
          to="/login"
          className="ml-4 bg-red-800 text-white font-bold py-2 px-4 rounded-full"
        >
          Login
        </Link>
      ) : (
        <>
          <Link
            to="/profile"
            className="ml-4 bg-blue-600 text-white font-bold py-2 px-4 rounded-full flex items-center"
          >
            <i className="fas fa-user mr-2"></i>
            <span>{fullName || 'Profile'}</span>
          </Link>
          <button
            onClick={handleLogout}
            className="ml-4 bg-gray-600 text-white font-bold py-2 px-4 rounded-full"
          >
            Logout
          </button>
        </>
      )}
    </div>
  );
}

export default Header;