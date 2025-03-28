import React from 'react';
import { NavLink } from 'react-router-dom';

function Sidebar() {
  return (
    <div className="bg-[#fef4e9] p-6 md:w-[15%] flex flex-col items-start">
      <div className="flex items-center mb-8">
        <i className="fas fa-seedling text-7xl mr-2"></i>
        <div className="flex flex-col">
          <span className="text-3xl font-bold">GOLMART</span>
          <span className="text-3xl font-bold">STORE</span>
        </div>
      </div>
      <nav className="flex flex-col space-y-4 w-full">
        <NavLink
          to="/"
          className={({ isActive }) =>
            isActive
              ? 'text-xl font-bold bg-[#d0e4a4] p-2 pl-6 rounded'
              : 'text-xl font-bold p-2 pl-6 rounded hover:bg-[#d0e4a4]'
          }
        >
          HOME
        </NavLink>
        <NavLink
          to="/cart"
          className={({ isActive }) =>
            isActive
              ? 'text-xl font-bold bg-[#d0e4a4] p-2 pl-6 rounded'
              : 'text-xl font-bold p-2 pl-6 rounded hover:bg-[#d0e4a4]'
          }
        >
          CART
        </NavLink>
        <NavLink
          to="/contact"
          className={({ isActive }) =>
            isActive
              ? 'text-xl font-bold bg-[#d0e4a4] p-2 pl-6 rounded'
              : 'text-xl font-bold p-2 pl-6 rounded hover:bg-[#d0e4a4]'
          }
        >
          CONTACT
        </NavLink>
        <NavLink
          to="/catalog"
          className={({ isActive }) =>
            isActive
              ? 'text-xl font-bold bg-[#d0e4a4] p-2 pl-6 rounded'
              : 'text-xl font-bold p-2 pl-6 rounded hover:bg-[#d0e4a4]'
          }
        >
          CATALOG
        </NavLink>
        <NavLink
          to="/testimonials"
          className={({ isActive }) =>
            isActive
              ? 'text-xl font-bold bg-[#d0e4a4] p-2 pl-6 rounded'
              : 'text-xl font-bold p-2 pl-6 rounded hover:bg-[#d0e4a4]'
          }
        >
          TESTIMONIALS
        </NavLink>
      </nav>
    </div>
  );
}

export default Sidebar;