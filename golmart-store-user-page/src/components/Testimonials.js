// src/components/Testimonials.js
import React from 'react';
import { Link } from 'react-router-dom';

function Testimonials() {
  return (
    <div className="flex-1 p-6">

      {/* Testimonials Content */}
      <div className="mb-8 px-10 pr-16">
        {/* Heading */}
        <div className="text-right w-full mb-8">
          <h1 className="text-8xl leading-tight fictional-friend">
            What Our Customers Say
          </h1>
        </div>

        {/* Testimonials Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {/* Testimonial 1 */}
          <div className="bg-[#ffdfba] p-4 rounded-lg shadow-lg">
            <img
              src="/images/testimonials/jane_doe.webp"
              alt="Jane Doe"
              className="w-60 h-60 rounded-full object-cover mb-4 block mx-auto"
            />
            <p className="font-bold mt-2 mb-2">- Jane Doe</p>
            <p className="italic text-gray-700">
              "I love shopping at Golmart Store! The products are always fresh and the service is excellent."
            </p>
          </div>
          {/* Testimonial 2 */}
          <div className="bg-[#ffdfba] p-4 rounded-lg shadow-lg">
            <img
              src="/images/testimonials/john_smith.webp"
              alt="John Smith"
              className="w-60 h-60 rounded-full object-cover mb-4 block mx-auto"
            />
            <p className="font-bold mt-2 mb-2">- John Smith</p>
            <p className="italic text-gray-700">
              "Best store in town! Highly recommend."
            </p>
          </div>
          {/* Testimonial 3 */}
          <div className="bg-[#ffdfba] p-4 rounded-lg shadow-lg">
            <img
              src="/images/testimonials/alice_johnson.webp"
              alt="Alice Johnson"
              className="w-60 h-60 rounded-full object-cover mb-4 block mx-auto"
            />
            <p className="font-bold mt-2 mb-2">- Alice Johnson</p>
            <p className="italic text-gray-700">
              "Great variety and quality products. Will definitely shop again."
            </p>
          </div>
        </div>

        {/* Call to Action */}
        <div className="text-center mt-8">
          <p className="text-xl">We value your feedback. Share your experience with us!</p>
          <br />
          <Link
            to="/contact"
            className="bg-green-200 text-black font-bold py-2 px-6 rounded-lg mt-8"
          >
            Contact Us
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Testimonials;