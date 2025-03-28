import React from 'react';

function Contact() {
  return (
    <div className="flex flex-col md:flex-row items-start md:items-center mb-8">
      {/* Contact Information */}
      <div className="md:w-1/2 mb-8 md:mb-0 px-10 pr-16">
        <div className="text-right w-full mb-8">
          <h1 className="text-8xl leading-tight fictional-friend">Contact Us</h1>
          <p className="text-3xl mt-8 mb-8 fictional-friend">We'd love to hear from you!</p>
        </div>
        <div className="space-y-4">
          <div className="flex items-center">
            <i className="fas fa-map-marker-alt text-2xl mr-4"></i>
            <p className="text-xl">557 Stone Lane, Reading</p>
          </div>
          <div className="flex items-center">
            <i className="fas fa-phone text-2xl mr-4"></i>
            <p className="text-xl">(123) 456-7890</p>
          </div>
          <div className="flex items-center">
            <i className="fas fa-envelope text-2xl mr-4"></i>
            <p className="text-xl">contact@golmartstore.com</p>
          </div>
        </div>
      </div>
      {/* Contact Form */}
      <div className="md:w-1/2 p-4 mr-8">
        <form className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-bold mb-2">Name</label>
            <input
              type="text"
              id="name"
              className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 w-full"
            />
          </div>
          <div>
            <label htmlFor="email" className="block text-sm font-bold mb-2">Email</label>
            <input
              type="email"
              id="email"
              className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 w-full"
            />
          </div>
          <div>
            <label htmlFor="message" className="block text-sm font-bold mb-2">Message</label>
            <textarea
              id="message"
              className="bg-[#f8d4ac] border border-gray-300 rounded-lg py-2 px-4 w-full"
              rows="4"
            ></textarea>
          </div>
          <button
            type="submit"
            className="bg-green-200 text-black font-bold py-2 px-6 rounded-lg"
          >
            Send Message
          </button>
        </form>
      </div>
    </div>
  );
}

export default Contact;