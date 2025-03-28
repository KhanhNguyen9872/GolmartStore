import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../css/home.css';
import { getUserInfo } from '../api';

function Home() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      getUserInfo(token)
        .then(() => {
          // API call succeeded, user is authenticated
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            localStorage.removeItem('token');
            setIsLoggedIn(false);
          }
        });
    }
  }, []);

  return (
    <div className="flex-1 p-6">
      <div className="flex flex-col md:flex-row items-start md:items-center mb-8">
        <div className="w-full mb-8 md:mb-0 px-10 pr-16">
          <div className="text-right w-full mb-8">
            <h1 className="text-8xl leading-tight fictional-friend">Spring Deals</h1>
            <h1 className="text-8xl leading-tight fictional-friend">Blooming Now!</h1>
            <p className="text-3xl mt-8 mb-8 fictional-friend">Special Sale starts today!</p>
          </div>
          <div className="flex flex-col md:flex-row items-start md:items-center mb-8">
            <div className="md:w-1/2 mb-8 md:mb-0">
              <h2 className="text-xl font-bold mb-4">557 STONE LANE, READING</h2>
              <p className="mb-4 pr-16">
                Welcome to Golmart Store, your destination for vibrant indoor plants. We offer a diverse selection of high-quality greenery to enhance your home or office. Our knowledgeable staff is here to assist with all your plant care needs.
              </p>
            </div>
            <div className="md:w-1/2 flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4 mb-4">
              <button className="bg-green-200 text-black font-bold py-2 px-6 rounded-lg">DISC. 50% ALL ITEMS</button>
              <button className="bg-red-200 text-black font-bold py-2 px-6 rounded-lg">BUY 1 GET 1 FOR SELECTED ITEMS</button>
              <button className="bg-yellow-200 text-black font-bold py-2 px-6 rounded-lg">GET THE GRAND PRIZES</button>
            </div>
          </div>
          {!isLoggedIn && (
            <div className="flex space-x-4">
              <Link to="/login" className="bg-red-400 text-white font-bold py-4 px-8 rounded-full text-center">LOG IN</Link>
              <Link to="/signup" className="bg-white text-black font-bold py-4 px-8 rounded-full border border-[#fc8181] text-center">SIGN UP</Link>
            </div>
          )}
        </div>
        <div className="md:w-1/2 flex justify-center p-4 mr-8">
          <img
            alt="Woman shopping with bags in hand, walking down a street"
            className="rounded-lg shadow-lg border-black border-4"
            height="1800"
            src="/images/home.jpg"
            width="600"
          />
        </div>
      </div>
    </div>
  );
}

export default Home;