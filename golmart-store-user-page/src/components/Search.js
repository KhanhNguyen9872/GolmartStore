import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { getSearchProduct } from '../api'; // Adjust the import path as needed

function Search() {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('q') || '';
  const [products, setProducts] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (query) {
      getSearchProduct(query)
        .then((data) => {
          setProducts(data);
          setError(null);
        })
        .catch((err) => {
          setError(err.message);
          setProducts([]);
        });
    } else {
      setProducts([]);
    }
  }, [query]);

  return (
    <div className="w-full mb-8 px-10">
      <h1 className="text-4xl font-bold mb-4">
        {query ? (
          <>
            <span>Search Results for '</span>
            <span>{query}</span>
            <span>'</span>
          </>
        ) : (
          <span>Search</span>
        )}
      </h1>
      <p
        className="text-center text-gray-500 mb-8"
        style={{ display: query ? 'none' : 'block' }}
      >
        Please use the search box above to find products.
      </p>
      {error && (
        <p className="text-center text-red-500 mb-8">{error}</p>
      )}
      <div
        className="grid grid-cols-1 md:grid-cols-3 gap-4"
        style={{ display: query ? 'grid' : 'none' }}
      >
        {products.length > 0 ? (
          products.map((product) => (
            <Link to={`/product/${product.id}`} key={product.id} className="block">
              <div className="bg-[#fcebd9] p-4 rounded-lg shadow-lg border-black border-4">
                {product.image ? (
                  <img
                    src={product.image}
                    alt={product.name}
                    className="w-full h-48 object-cover mb-2 rounded"
                    onError={(e) => { e.target.src = '/images/default-product.jpg'; }} // Fallback image
                  />
                ) : (
                  <div className="w-full h-48 bg-gray-300 mb-2 rounded"></div> // Placeholder if no image
                )}
                <h3 className="text-lg font-bold">{product.name}</h3>
                <p className="text-gray-600 mt-2">{product.description || 'No description'}</p>
                <p className="text-lg font-bold text-green-700 mt-2">${product.price.toFixed(2)}</p>
              </div>
            </Link>
          ))
        ) : (
          query && (
            <p className="text-center text-gray-500 col-span-full">
              No products found for '{query}'.
            </p>
          )
        )}
      </div>
    </div>
  );
}

export default Search;