import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCartInfo, getProductById, removeFromCart, updateCartQuantity } from '../api';

function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      getCartInfo(token)
        .then((cartData) => {
          if (cartData.length === 0) {
            setCartItems([]);
            setIsLoggedIn(true);
            return;
          }
          // Get product IDs from cart
          const productIds = cartData.map((item) => item.productId);
          // Fetch product details for all product IDs
          Promise.all(productIds.map((id) => getProductById(id)))
            .then((products) => {
              // Create a map of productId to product details
              const productMap = products.reduce((map, product) => {
                map[product.id] = product;
                return map;
              }, {});
              // Enrich cart items with price
              const enrichedCartItems = cartData.map((item) => ({
                ...item,
                price: productMap[item.productId].price || 0,
              }));
              setCartItems(enrichedCartItems);
              setIsLoggedIn(true);
            })
            .catch((error) => {
              console.error('Failed to fetch product details:', error);
              setCartItems([]);
              setIsLoggedIn(true); // Still logged in, but show empty cart
            });
        })
        .catch((error) => {
          if (error.message.includes('401')) {
            localStorage.removeItem('token');
            setIsLoggedIn(false);
            setCartItems([]);
            navigate('/login'); // Redirect to login on 401
          } else {
            console.error('Failed to fetch cart info:', error);
            setCartItems([]);
            setIsLoggedIn(true); // Assume logged in but cart fetch failed
          }
        });
    } else {
      setIsLoggedIn(false);
      setCartItems([]);
    }
  }, [navigate]);

  // Handle removing an item from the cart
  const handleRemove = async (productId) => {
    const token = localStorage.getItem('token');
    try {
      await removeFromCart(token, productId);
      // Update local state by removing the item
      setCartItems(cartItems.filter((item) => item.productId !== productId));
    } catch (error) {
      console.error('Failed to remove product:', error);
      alert('Failed to remove product from cart. Please try again.');
    }
  };

  // Handle updating the quantity of an item
  const handleQuantityChange = async (productId, newQuantity) => {
    const token = localStorage.getItem('token');
    const parsedQuantity = parseInt(newQuantity, 10);
    if (parsedQuantity < 1 || isNaN(parsedQuantity)) return; // Prevent invalid quantities

    try {
      await updateCartQuantity(token, productId, parsedQuantity);
      // Update local state with the new quantity
      setCartItems(
        cartItems.map((item) =>
          item.productId === productId ? { ...item, quantity: parsedQuantity } : item
        )
      );
    } catch (error) {
      console.error('Failed to update quantity:', error);
      alert('Failed to update quantity. Please try again.');
    }
  };

  // Case 1: User is not logged in
  if (!isLoggedIn) {
    return (
      <div className="mb-8 px-10 pr-16">
        <div className="text-right w-full mb-8">
          <h1 className="text-8xl leading-tight fictional-friend">Your cart</h1>
          <p className="text-3xl mt-8 mb-8 fictional-friend">Please log in to view your shopping cart</p>
        </div>
      </div>
    );
  }

  // Case 2: User is logged in but cart is empty
  if (cartItems.length === 0) {
    return (
      <div className="mb-8 px-10 pr-16">
        <div className="text-right w-full mb-8">
          <h1 className="text-8xl leading-tight fictional-friend">Your cart</h1>
          <p className="text-3xl mt-8 mb-8 fictional-friend">Your cart is empty</p>
        </div>
      </div>
    );
  }

  // Case 3: User is logged in and cart has items
  const subtotal = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const tax = subtotal * 0.10; // Assume 10% tax rate
  const total = subtotal + tax;

  return (
    <div className="mb-8 px-10 pr-16">
      <div className="text-right w-full mb-8">
        <h1 className="text-8xl leading-tight fictional-friend">Your cart</h1>
        <p className="text-3xl mt-8 mb-8 fictional-friend">Browse products in your shopping cart</p>
      </div>
      <table className="bg-[#fcebd9] w-full mb-4 border-collapse">
        <thead>
          <tr className="bg-gray-200">
            <th className="p-2 text-left font-bold">Product</th>
            <th className="p-2 text-left font-bold">Price</th>
            <th className="p-2 text-left font-bold">Quantity</th>
            <th className="p-2 text-left font-bold">Total</th>
            <th className="p-2 text-left font-bold">Actions</th>
          </tr>
        </thead>
        <tbody>
          {cartItems.map((item) => (
            <tr key={item.productId} className="border-b">
              <td className="p-2">{item.productName}</td>
              <td className="p-2">${item.price.toFixed(2)}</td>
              <td className="p-2">
                <input
                  type="number"
                  value={item.quantity} // Use value instead of defaultValue for controlled input
                  min="1"
                  onChange={(e) => handleQuantityChange(item.productId, e.target.value)}
                  className="w-16 p-1 border rounded focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </td>
              <td className="p-2">${(item.price * item.quantity).toFixed(2)}</td>
              <td className="p-2">
                <button
                  onClick={() => handleRemove(item.productId)}
                  className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                >
                  Remove
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="flex justify-end">
        <div className="w-1/3 bg-gray-100 p-4 rounded">
          <div className="flex justify-between mb-2">
            <span>Subtotal</span>
            <span>${subtotal.toFixed(2)}</span>
          </div>
          <div className="flex justify-between mb-2">
            <span>Discount</span>
            <span>$0.00</span>
          </div>
          <div className="flex justify-between mb-2">
            <span>Tax (10%)</span>
            <span>${tax.toFixed(2)}</span>
          </div>
          <div className="flex justify-between font-bold mb-4">
            <span>Total</span>
            <span>${total.toFixed(2)}</span>
          </div>
          <button className="bg-green-500 text-white px-4 py-2 rounded w-full hover:bg-green-600">
            Proceed to Checkout
          </button>
        </div>
      </div>
    </div>
  );
}

export default Cart;