-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 30, 2025 lúc 01:20 PM
-- Phiên bản máy phục vụ: 8.0.40
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `golmartstore_db`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `admins`
--

CREATE TABLE `admins` (
  `id` bigint NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `admins`
--

INSERT INTO `admins` (`id`, `full_name`, `username`, `password`, `email`, `enabled`) VALUES
(1, 'Admin User1', 'admin1', '$2a$12$YNqySO48RoULQTRRMyY85eivC5gcXhDkpU8Uf/RoBPJZ6BqtTxC9G', 'admin@example.com', 1),
(4, 'System User', 'system', '$2a$10$n42c5s9VxNnlVMC22S5b5.qn5wEcvuxBeAW/3e3DbRQRiJDVVdc1G', 'system@example.com', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `authorities`
--

CREATE TABLE `authorities` (
  `id` bigint NOT NULL,
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `authorities`
--

INSERT INTO `authorities` (`id`, `username`, `authority`) VALUES
(14, 'system', 'ROLE_ADMIN'),
(15, 'system', 'ROLE_SYSTEM'),
(17, 'admin1', 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `carts`
--

CREATE TABLE `carts` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `carts`
--

INSERT INTO `carts` (`id`, `user_id`) VALUES
(1, 1),
(2, 3),
(3, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart_info`
--

CREATE TABLE `cart_info` (
  `cart_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `cart_info`
--

INSERT INTO `cart_info` (`cart_id`, `product_id`, `quantity`) VALUES
(1, 1, 2),
(2, 1, 1),
(3, 1, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `id` bigint NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text,
  `price` decimal(10,2) NOT NULL,
  `details` text,
  `more_about` text,
  `image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `products`
--

INSERT INTO `products` (`id`, `name`, `description`, `price`, `details`, `more_about`, `image`) VALUES
(1, 'Aglaonema Red', 'Vibrant, easy-care houseplant with striking red leaves.', 29.99, '- Feature 1: High-quality material\r\n- Feature 2: Durable design\r\n- Feature 3: Available in multiple colors', 'Red Aglaonema, also known as Chinese Evergreen, is a popular houseplant celebrated for its vibrant red and green foliage. Native to tropical and subtropical regions of Asia, this plant is favored for its striking appearance and ease of care.', '/img/ae902d4b-453e-4787-9b48-6bb28580aa8a.jpg'),
(2, 'Aglaonema Red Sumatra', 'Eye-catching, low-maintenance houseplant with bold red and green leaves.', 34.99, '- Feature 1: High-quality material\r\n- Feature 2: Robust and elegant design\r\n- Feature 3: Suitable for indoor decoration', 'Aglaonema Red Sumatra is a stunning variety of the Chinese Evergreen, renowned for its vibrant red and lush green foliage. Originating from tropical regions, this plant is appreciated for its unique appearance and effortless maintenance, making it a favorite among indoor plant enthusiasts.', '/img/edad1bbc-871c-489c-925d-91af39ee80bc.jpg'),
(3, 'Caladium bicolor', 'Striking ornamental plant known for its vibrant, heart-shaped leaves.', 24.99, '- Feature 1: High-quality foliage\r\n- Feature 2: Colorful and unique leaf patterns\r\n- Feature 3: Perfect for indoor and outdoor decor', 'Caladium Bicolor, commonly known as Heart of Jesus or Angel Wings, is a popular decorative plant admired for its bold, colorful leaves. Native to South America, this plant features an array of vibrant patterns, making it an eye-catching addition to any garden or indoor space.', '/img/ab32ba3e-cf50-434c-a09e-9cf063373157.jpg'),
(4, 'Aglaonema Rotundum Pink', 'Elegant houseplant with lush, rounded leaves accented by striking pink veins.', 32.99, '- Feature 1: High-quality foliage\r\n- Feature 2: Unique pink and green contrast\r\n- Feature 3: Ideal for indoor decoration', 'Aglaonema Rotundum Pink, a beautiful variety of the Chinese Evergreen, is cherished for its rounded, dark green leaves highlighted by vibrant pink veins. Native to tropical regions, this plant is a favorite among plant enthusiasts for its distinctive appearance and easy-care nature.', '/img/11f430ed-0dea-4e20-9e7d-bda455758f19.jpg'),
(5, 'Perilla frutescens', 'Versatile herb known for its aromatic leaves and unique flavor.', 19.99, '- Feature 1: High-quality, fragrant foliage\r\n- Feature 2: Rich in essential nutrients\r\n- Feature 3: Suitable for culinary and ornamental use', 'Perilla Frutescens, commonly known as Shiso or Beefsteak Plant, is an herbaceous plant native to East Asia. Renowned for its aromatic leaves with a hint of mint and basil, this plant is widely used in Asian cuisine and valued for its medicinal properties and decorative appeal.', '/img/3423cf85-67e0-4188-8cbb-f376b5d4e149.jpg'),
(6, 'Calathea', 'Stunning houseplant known for its vibrant, patterned leaves and graceful movements.', 27.99, '- Feature 1: High-quality, decorative foliage\r\n- Feature 2: Unique leaf patterns and colors\r\n- Feature 3: Ideal for indoor environments', 'Calathea, often referred to as the Prayer Plant, is cherished for its striking, patterned leaves that fold up at night, resembling hands in prayer. Native to tropical regions of South America, this plant is loved for its dynamic appearance and ability to thrive indoors with proper care.', '/img/dc3b99e1-cea5-4448-8ad3-a9ea8ca6bf6e.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `full_name`, `username`, `password`, `email`, `enabled`) VALUES
(1, 'John Doe', 'johndoe', '$2a$12$YNqySO48RoULQTRRMyY85eivC5gcXhDkpU8Uf/RoBPJZ6BqtTxC9G', 'john.doe@example.com', 1),
(3, 'Nguyen Van Khanh', 'khanh1', '$2a$10$8j2uaiSFjGkgYZt8HJy2TeuQrJ/hpkJXLIFn1hU0M52Bjzb/tD566', 'khanh1@gmail.com', 1),
(4, 'Nguyen Van Khanh 2', 'khanh2', '$2a$10$wPAckb3uXS7jv/QWfAJu/eCQJ6AGAFg2pgL6hGqZYIzBwbapnPD.W', 'khanh2@gmail.com', 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Chỉ mục cho bảng `authorities`
--
ALTER TABLE `authorities`
  ADD PRIMARY KEY (`id`),
  ADD KEY `authorities_ibfk_1` (`username`);

--
-- Chỉ mục cho bảng `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `cart_info`
--
ALTER TABLE `cart_info`
  ADD PRIMARY KEY (`cart_id`,`product_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `admins`
--
ALTER TABLE `admins`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `authorities`
--
ALTER TABLE `authorities`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT cho bảng `carts`
--
ALTER TABLE `carts`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Ràng buộc đối với các bảng kết xuất
--

--
-- Ràng buộc cho bảng `authorities`
--
ALTER TABLE `authorities`
  ADD CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `admins` (`username`) ON DELETE CASCADE;

--
-- Ràng buộc cho bảng `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `carts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Ràng buộc cho bảng `cart_info`
--
ALTER TABLE `cart_info`
  ADD CONSTRAINT `cart_info_ibfk_1` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `cart_info_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
