import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Home from './components/Home';
import Testimonials from './components/Testimonials';
import Cart from './components/Cart';
import Contact from './components/Contact';
import Login from './components/Login';
import Signup from './components/Signup';
import ForgotPassword from './components/ForgotPassword';
import Catalog from './components/Catalog';
import Product from './components/Product';
import Search from './components/Search';
import Profile from './components/Profile';
import './css/home.css';

function App() {
  return (
    <Router>
      <div className="bg-[#fef4e9] text-black p-6 relative min-h-screen" style={{ zIndex: '0' }}>
        <div className="background-images">
          <img src="/images/leaf.png" className="decorative-img leaf1" alt="Leaf decoration" />
          <img src="/images/leaf_1.png" className="decorative-img leaf2" alt="Leaf decoration" />
          <img src="/images/leaf_2.png" className="decorative-img leaf3" alt="Leaf decoration" />
          <img src="/images/clover.png" className="decorative-img clover" alt="Clover decoration" />
          <img src="/images/leaf-fall.png" className="decorative-img leaf-fall" alt="Falling leaf decoration" />
          <img src="/images/maple-leaf.png" className="decorative-img maple-leaf" alt="Maple leaf decoration" />
        </div>
        <div className="flex flex-col md:flex-row min-h-screen relative z-10">
          <Sidebar />
          <div className="flex-1 p-6">
            <Header />
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/testimonials" element={<Testimonials />} />
              <Route path="/cart" element={<Cart />} />
              <Route path="/contact" element={<Contact />} />
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/catalog" element={<Catalog />} />
              <Route path="/product/:id" element={<Product />} />
              <Route path="/search" element={<Search />} />
              <Route path="/profile" element={<Profile />} />
            </Routes>
          </div>
        </div>
      </div>
    </Router>
  );
}

export default App;