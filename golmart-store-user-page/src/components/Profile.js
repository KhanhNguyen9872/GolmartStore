// src/Profile.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUserInfo } from '../api'; // Adjust the path if api.js is in a different directory

function Profile() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/login'); // Redirect to login if no token
          return;
        }
        const data = await getUserInfo(token);
        setUser(data);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchUserData();
  }, [navigate]);

  // Loading state
  if (loading) {
    return <div className="flex-1 p-6 text-center text-xl">Loading...</div>;
  }

  // Error state
  if (error) {
    return (
      <div className="flex-1 p-6 text-center text-xl text-red-500">
        Error: {error}
      </div>
    );
  }

  // Success state: Display user profile
  return (
    <div className="flex-1 p-6">
      <h1 className="text-5xl font-bold mb-6">Profile</h1>
      <div className="bg-[#fcebd9] shadow-lg rounded-lg p-6 border border-gray-200">
        <p className="mb-4 text-xl">
          <strong>Username:</strong> {user.username}
        </p>
        <p className="mb-4 text-xl">
          <strong>Full Name:</strong> {user.fullName}
        </p>
        <p className="mb-4 text-xl">
          <strong>Email:</strong> {user.email}
        </p>
      </div>
    </div>
  );
}

export default Profile;