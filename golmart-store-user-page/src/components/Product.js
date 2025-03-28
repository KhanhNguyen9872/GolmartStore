import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { getProductById, addToCart } from '../api'; // Adjust path if necessary

function Product() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [popupVisible, setPopupVisible] = useState(false);
  const [popupMessage, setPopupMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const data = await getProductById(id);
        setProduct(data);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  const handleAddToCart = async () => {
    const token = localStorage.getItem('token'); // Assuming token is stored in localStorage
    if (!token) {
      setPopupMessage('Please log in to add items to your cart.');
      setPopupVisible(true);
      return;
    }

    try {
      await addToCart(token, product.id, 1); // Quantity defaults to 1
      setPopupMessage('Product added to cart successfully!');
      setPopupVisible(true);
    } catch (err) {
      setPopupMessage(err.message);
      setPopupVisible(true);
    }
  };

  const Popup = () => {
    const isLoginRequired = popupMessage.includes('log in'); // Check if popup is for login prompt

    return (
      <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
        <div className="bg-white p-6 rounded-lg shadow-lg">
          <p className="text-lg mb-4">{popupMessage}</p>
          <div className="flex justify-end space-x-4">
            {isLoginRequired ? (
              <>
                <button
                  onClick={() => setPopupVisible(false)}
                  className="bg-gray-300 text-black px-4 py-2 rounded hover:bg-gray-400"
                >
                  Close
                </button>
                <button
                  onClick={() => navigate('/login')}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  Go to Login
                </button>
              </>
            ) : (
              <>
                <button
                  onClick={() => setPopupVisible(false)}
                  className="bg-gray-300 text-black px-4 py-2 rounded hover:bg-gray-400"
                >
                  Continue Shopping
                </button>
                <button
                  onClick={() => navigate('/cart')}
                  className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                >
                  Go to Cart
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    );
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!product) {
    return <div>Product not found</div>;
  }

  return (
    <div className="mb-8 px-10 pr-16">
      <div className="mb-4 flex items-center space-x-4">
        <Link
          to="/catalog"
          className="bg-green-200 text-black font-bold py-2 px-4 rounded-lg hover:bg-green-300 transition"
        >
          <i className="fas fa-arrow-left mr-2"></i> Back to Catalog
        </Link>
      </div>
      <div className="flex flex-col md:flex-row items-start">
        <div className="md:w-1/2 mb-8 md:mb-0">
          <img
            src={product.image}
            alt={product.name}
            className="w-full h-96 object-cover rounded-lg shadow-lg"
          />
        </div>
        <div className="md:w-1/2 md:pl-8">
          <h1 className="text-5xl font-bold mb-4 fictional-friend">{product.name}</h1>
          <p className="text-xl text-gray-600 mb-4">{product.description}</p>
          <p className="text-2xl font-bold mb-4">${product.price.toFixed(2)}</p>
          {product.details && (
            <div className="mb-4">
              <p className="text-lg font-bold">Details:</p>
              <p className="text-gray-700">{product.details}</p>
            </div>
          )}
          <button
            onClick={handleAddToCart}
            className="bg-green-200 text-black font-bold py-2 px-6 rounded-lg"
          >
            Add to Cart
          </button>
        </div>
      </div>
      {product.moreAbout && (
        <div className="mt-8">
          <h2 className="text-2xl font-bold mb-2">More About {product.name}</h2>
          <p className="text-gray-700">{product.moreAbout}</p>
        </div>
      )}
      {popupVisible && <Popup />}
    </div>
  );
}

export default Product;